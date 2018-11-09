package com.leiyuntop.machine;

import com.leiyuntop.util.FileUtil;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android_serialport_api.SerialPort;

/**
 * 售货机管理类
 * 
 * @author MENGQINGYU
 * @version 1.0.0
 */
public class StatusCapture implements Runnable
{
	private SerialPort m_Port;
	private boolean m_Exit;

	private int m_Version1 = 0;
	private int m_Version2 = 0;

	private int m_Number = 0;

	private int m_NotesInit;
	private int m_CoinsInit;
	private int m_NotesState = -1;
	private int m_CoinsState = -1;
	private int m_DoorOpen = -100;
	private int m_CoinsQty;
	private int m_Amount;
	private int m_CoinsRturn = -1;

	private boolean m_Setting;
	private int m_MotorQty = 0;
	private boolean m_Elevator = false;
	private int m_ElevatorLay = 0;
	private LinkedHashMap<String, int[]> m_ElevatorRun = new LinkedHashMap<String, int[]>();
	private String m_ElevatorLocation = "W";
	private int m_ElevatorNumber = 0;

	private int m_PickCheck = -1;
	private boolean m_PickTest = false;
	private boolean m_Init = true;

	private boolean m_AutoCloseDoor = true;

	private LinkedHashMap<String, SCState> m_State;

	private List<ISCInfo> m_Info = new ArrayList<ISCInfo>();
	private List<ErrorData> m_Error = new ArrayList<ErrorData>();
	private List<int[]> m_Command = new ArrayList<int[]>();
	private List<Integer> m_Cache = new ArrayList<Integer>();
	private List<Byte> m_LayCheck = new ArrayList<Byte>();

	private int m_Column;

	private Timer m_Timer = new Timer();
	private Timer m_Timer1 = new Timer();

	private IMachineEventHandler m_EventHandler;

	/**
	 * 自动锁门状态
	 * 
	 * @return 值
	 */
	public boolean getAutoCloseDoor()
	{
		return m_AutoCloseDoor;
	}

	/**
	 * 自动锁门状态
	 * 
	 * @param val
	 *            值
	 */
	public void setAutoCloseDoor(boolean val)
	{
		m_AutoCloseDoor = val;
	}

	/**
	 * 构造函数
	 */
	public StatusCapture()
	{
		m_ElevatorRun.put("A", new int[] { 0xAA, 0x25, 0x02, 0x66, 0x41, 0xBB });
		m_ElevatorRun.put("B", new int[] { 0xAA, 0x26, 0x02, 0x66, 0x42, 0xBB });
		m_ElevatorRun.put("C", new int[] { 0xAA, 0x27, 0x02, 0x66, 0x43, 0xBB });
		m_ElevatorRun.put("D", new int[] { 0xAA, 0x20, 0x02, 0x66, 0x44, 0xBB });
		m_ElevatorRun.put("E", new int[] { 0xAA, 0x21, 0x02, 0x66, 0x45, 0xBB });
		m_ElevatorRun.put("F", new int[] { 0xAA, 0x22, 0x02, 0x66, 0x46, 0xBB });
		m_ElevatorRun.put("Y", new int[] { 0xAA, 0x3D, 0x02, 0x66, 0x59, 0xBB });
		m_ElevatorRun.put("Z", new int[] { 0xAA, 0x3E, 0x02, 0x66, 0x5A, 0xBB });
	}

	/**
	 * 析构函数
	 */
	@Override
	protected void finalize()
	{
		Close();
	}

	/**
	 * 开始监听
	 * 
	 * @param portName
	 *            串口号

	 *            最大列数
	 */
	public boolean Start(String portName, int column)
	{
		try
		{
			m_Port = new SerialPort(new File(portName), 9600, 0);

			m_Column = column;

			m_MotorQty = 0;
			m_State = new LinkedHashMap<String, SCState>();

			m_Timer.schedule(new InfoTask(this), 100, 100);
			m_Timer1.schedule(new HeartTask(this), 30000, 30000);

			m_Init = true;
			m_Exit = false;

			Thread thread = new Thread(this);
			thread.start();

			m_Command.add(new int[] { 0xAA, 0x68, 0x03, 0x6D, 0x00, 0x06, 0xBB });

			// 得到电机状态
			for (int i = 0xA; i <= 0xF; i++)
			{
				for (int j = 1; j <= column; j++)
				{
					int[] val = new int[] { 0xAA, 0x00, 0x02, 0x63, 0x00, 0xBB };

					val[4] = (i << 4) + j % 10;
					val[1] = val[2] ^ val[3] ^ val[4];

					m_Command.add(val);
				}
			}

			// 得到电机数
			m_Command.add(new int[] { 0xAA, 0x9E, 0x02, 0x63, 0xFF, 0xBB });
			m_Command.add(new int[] { 0xAA, 0x68, 0x03, 0x6D, 0x00, 0x06, 0xBB });

			m_NotesState = -1;
			m_CoinsState = -1;

			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * 结束监听
	 */
	public void Close()
	{
		m_Timer.cancel();
		m_Timer1.cancel();
		m_Exit = true;

		if (m_Port != null)
		{
			m_Port.close();
			m_Port = null;
		}
	}

	/**
	 * 售货机事件
	 * 
	 * @param event
	 *            事件处理类
	 */
	public void SetEventHandler(IMachineEventHandler event)
	{
		m_EventHandler = event;
	}

	@Override
	public void run()
	{
		List<Integer> data = new ArrayList<Integer>();
		int val = 0;
		int len = 100;
		int index = 0;

		InputStream inputStream = m_Port.getInputStream(); // 从串口来的输入流
		OutputStream outputStream = m_Port.getOutputStream(); // 向串口输出的流

		while (!m_Exit)
		{
			try
			{
				int size = inputStream.available();
				
				if (size > 0)
				{
					val = inputStream.read();

					if (val == 0xCC && data.size() == 0)
					{
						data.clear();
						len = 100;
						index = 0;
					}

					if (val == 0xCC || data.size() != 0)
					{
						data.add(val);
						index++;
					}

					if (index == 3)
					{
						len = val + 4;
					}

					if (val == 0xBB)
					{
						if (val == 0xBB && index >= len)
						{
							if (data.get(3) == 0x60 || CheckCommand(data))
							{
								byte[] rtn = ExecCommand(data);

								if (rtn != null && rtn.length > 0)
								{
									outputStream.write(rtn);
								}
							}

							data.clear();
							len = 100;
							index = 0;
						}
					}
				}
				else
				{
					Thread.sleep(5);
				}
			}
			catch (Exception ex)
			{

			}
		}
	}

	/**
	 * 执行命令
	 * 
	 * @param data
	 *            命令字
	 * @return 返回数据
	 */
	private byte[] ExecCommand(List<Integer> data)
	{
		
		int[] rtn = new int[0];

		exit:
		{
			try
			{
				if (data.size() < 5)
				{
					break exit;
				}

				int len = data.get(2);
				int check = 0;

				for (int i = 2; i < len + 3; i++)
				{
					check ^= data.get(i);
				}

				if (check != data.get(1))
				{
					break exit;
				}

				if (data.get(len + 3) != 0xBB)
				{
					break exit;
				}
				if(data.get(3) != 0x60){
					StringBuffer sb = new StringBuffer();
					for(int i = 0;i < data.size();i++){
						sb.append(Integer.toHexString(data.get(i)) + " ");
					}
					//TODO
					FileUtil.saveCrashInfo2File("接受data ＝ " + sb);

				}
				if (data.get(3) == 0x60)
				{
					/*
					 * 握手指令
					 */
					
					if (m_NotesState != data.get(4))
					{
						m_NotesState = data.get(4);

						m_Info.add(new SCNotesStatus(GetNotesState(), 0, 0, m_NotesState == 0xFF));

						if (data.get(4) == 0x77)
						{
							m_Error.add(new ErrorData(13, "纸币器故障"));
						}
					}

					if (m_CoinsState != data.get(5))
					{
						m_CoinsState = data.get(5);

						m_Info.add(new SCCoinStatus(GetCoinsState(), 0, 0, m_CoinsState == 0xFF));

						if (data.get(5) == 0x77)
						{
							m_Error.add(new ErrorData(12, "硬币器故障"));
						}
					}

					if (data.get(8) == 0x34)
					{
						m_Info.add(new SCTagButton(1));
					}
					else if (data.get(8) == 0x24)
					{
						m_Info.add(new SCTagButton(2));
					}
					else if (data.get(8) == 0x11)
					{
						m_Info.add(new SCTagButton(3));
					}
					else if (data.get(8) == 0x21)
					{
						m_Info.add(new SCTagButton(4));
					}
					else if (data.get(8) == 0xEE)
					{
						m_Command.add(new int[] { 0xAA, 0x68, 0x03, 0x6D, 0x00, 0x06, 0xBB });
					}
					else if (data.get(8) == 0x00 || data.get(8) == 0xFF)
					{
						if (m_DoorOpen != data.get(8))
						{
							m_DoorOpen = data.get(8);
							m_Info.add(new SCDoorSwitch(data.get(8) == 0xFF));
						}
					}

					m_Elevator = data.get(8) == 0xFF;

					if (m_PickCheck != data.get(7))
					{
						m_PickCheck = data.get(7);
						m_Info.add(new SCPickStatusStatus(new int[8], m_PickCheck == 0xFF ? SCState.正常 : SCState.无设备));
					}

					if (m_Version1 == 1)
					{
						m_Number++;
						m_Number %= 10;

						if (m_Number == 0 && m_Command.size() < 3)
						{
							m_Command.add(new int[] { 0xAA, 0x65, 0x02, 0x67, 0x00, 0xBB });
						}
					}

					if (m_Command.size() > 0)
					{
						rtn = m_Command.get(0);
						m_Command.remove(0);
						StringBuffer sb = new StringBuffer();
						for(int i = 0;i < rtn.length;i++){
							sb.append(Integer.toHexString(rtn[i]) + " ");
						}
						FileUtil.saveCrashInfo2File("发送指令给单片机 ＝ " + sb.toString());
//TODO
					}
					else
					{
						rtn = new int[] { 0xAA, 0x62, 0x02, 0x60, 0x00, 0xBB };
					}
				}
				else if (data.get(3) == 0x63 && data.get(2) == 0x02)
				{
					/*
					 * 电机数量
					 */

					m_Command.add(new int[] { 0xAA, 0x68, 0x03, 0x6D, 0x00, 0x06, 0xBB });

					m_Init = false;
					m_Info.add(new SCInit(HexToBCD(data.get(4)), m_State));

					break exit;
				}
				else if (data.get(3) == 0x63)
				{
					/*
					 * 货道状态
					 */

					String lay = Integer.toHexString(data.get(5)).toUpperCase();

					m_State.put(lay, data.get(6) == 0xFF ? SCState.正常
							: (data.get(6) == 0x66 ? SCState.异常 : SCState.无设备));

					if (data.get(6) != 0xFF)
					{
						if (!m_Init)
						{
							m_Info.add(new SCCargoRoadFault(lay, data.get(6) == 0x66 ? SCLayState.电机不到位
									: SCLayState.无电机));
							m_Error.add(new ErrorData(5, lay + (data.get(6) == 0x66 ? "电机不到位" : "无电机")));
						}
					}

					break exit;
				}
				else if (data.get(3) == 0x61 && data.get(2) == 0x06 && data.get(4) == 0x01)
				{
					/*
					 * 纸币器预收币
					 */

					m_Info.add(new SCCoinBefore(HexToBCD(data.get(6)) * 10000 + HexToBCD(data.get(7)) * 100
							+ HexToBCD(data.get(8))));
				}
				else if (data.get(3) == 0x61 && data.get(2) == 0x06 && data.get(4) == 0x02)
				{
					/*
					 * 纸币器收币
					 */

					m_Info.add(new SCCoinCollection(CSNotesCoins.纸币器, HexToBCD(data.get(6)) * 10000
							+ HexToBCD(data.get(7)) * 100 + HexToBCD(data.get(8))));
				}
				else if (data.get(3) == 0x61 && data.get(2) == 0x08)
				{
					/*
					 * 纸币器状态
					 */

					int input = HexToBCD(data.get(5)) * 10000 + HexToBCD(data.get(6)) * 100 + HexToBCD(data.get(7));
					int wait = HexToBCD(data.get(8)) * 10000 + HexToBCD(data.get(9)) * 100 + HexToBCD(data.get(10));

					m_Info.add(new SCNotesStatus(GetNotesState(), input, wait, m_NotesState == 0xFF));
				}
				else if (data.get(3) == 0x62 && data.get(2) == 0x06)
				{
					/*
					 * 硬币收币
					 */

					m_Info.add(new SCCoinCollection(CSNotesCoins.硬币器, HexToBCD(data.get(6)) * 10000
							+ HexToBCD(data.get(7)) * 100 + HexToBCD(data.get(8))));
				}
				else if (data.get(3) == 0x62 && data.get(2) == 0x08)
				{
					/*
					 * 硬币器状态
					 */

					int input = HexToBCD(data.get(5)) * 10000 + HexToBCD(data.get(6)) * 100 + HexToBCD(data.get(7));
					int qty = HexToBCD(data.get(8)) * 10000 + HexToBCD(data.get(9)) * 100 + HexToBCD(data.get(10));

					if (m_CoinsRturn == 1)
					{
						/*
						 * 退币完成计算
						 */

						m_Info.add(new SCCoinReturnQty(m_CoinsQty - qty));
						m_CoinsRturn = -1;
						m_Amount = 0;
					}
					else if (m_CoinsRturn == 0)
					{
						/*
						 * 退币指令
						 */

						int[] temp = BCDToHex(m_Amount, 3);

						int[] val = new int[] { 0xAA, 0x00, 0x05, 0x62, 0x66, 0x00, 0x00, 0x00, 0xBB };
						val[5] = temp[0];
						val[6] = temp[1];
						val[7] = temp[2];
						val[1] = val[2] ^ val[3] ^ val[4] ^ val[5] ^ val[6] ^ val[7];

						m_Command.add(val);
					}
					else
					{
						/*
						 * 硬币器状态
						 */

						m_Info.add(new SCCoinStatus(GetCoinsState(), qty, input, m_CoinsState == 0xFF));
					}

					m_CoinsQty = qty;
				}
				else if (data.get(3) == 0x62 && data.get(2) == 0x02)
				{
					/*
					 * 硬币器退币
					 */

					if (m_CoinsRturn != -1)
					{
						m_CoinsRturn = 1;
						m_Command.add(new int[] { 0xAA, 0xE8, 0x02, 0x62, 0x88, 0xBB });
					}
				}
				else if (data.get(3) == 0x62 && data.get(2) == 0x03 && data.get(4) == 0x02)
				{
					/*
					 * 退币按钮
					 */

					m_Info.add(new SCCoinReturn());
				}
				else if (data.get(3) == 0x62 && data.get(2) == 0x03 && data.get(4) == 0x03)
				{
					/*
					 * 附加按钮
					 */

					m_Info.add(new SCTagButton(1));
				}
				else if (data.get(3) == 0x62 && data.get(2) == 0x16)
				{
					/*
					 * 硬币数量
					 */

					Hashtable<Double, Integer> coin = new Hashtable<Double, Integer>();

					double rate = data.get(5) * Math.pow(10, data.get(6));

					for (int i = 0; i < 8; i++)
					{
						double money = data.get(8 + i) * rate;

						if (((data.get(7) >> (7 - i)) & 0x01) != 0)
						{
							if (!coin.containsKey(money))
							{
								coin.put(money, 0);
							}

							coin.put(money, coin.get(money) + data.get(17 + i));
						}
					}

					m_Info.add(new SCCoinQty(coin));
				}
				else if (data.get(3) == 0x64 && data.get(2) == 0x06)
				{
					/*
					 * 付货结果
					 */

					String key = Integer.toHexString(data.get(5)).toUpperCase();

					SCSaleState state;
					int state1 = data.get(7);
					int state2 = data.get(8);

					if ((state1 & SCSaleIrState.有红外) == SCSaleIrState.有红外)
					{
						state = (state1 & SCSaleIrState.检测失败) == SCSaleIrState.检测失败 ? SCSaleState.付货失败
								: SCSaleState.付货正常;
					}
					else
					{
						state = state2 == SCSaleMotorState.正常 ? SCSaleState.付货正常 : SCSaleState.付货失败;
					}

					String val1 = "";
					String val2 = "";

					for (int i = 0; i < 8; i++)
					{
						val1 += (state1 & 0x01);
						val2 += (state2 & 0x01);

						state1 >>= 1;
						state2 >>= 1;
					}

					m_Info.add(new SCSale(key, state, val1, val2));
				}
				else if (data.get(3) == 0x69)
				{
					/*
					 * 纸币器初始化
					 */

					m_NotesInit = data.get(4) == 0x88 ? 1 : 0;
				}
				else if (data.get(3) == 0x6A)
				{
					/*
					 * 硬币器初始化
					 */

					m_CoinsInit = data.get(4) == 0x88 ? 1 : 0;
				}
				else if (data.get(3) == 0x66)
				{
					/*
					 * 升降机位置测试
					 */

					int[] val = new int[] { 0xAA, 0x00, 0x02, 0x67, 0x00, 0xBB };

					val[4] = data.get(4);
					val[1] = val[2] ^ val[3] ^ val[4];

					m_Command.add(val);
				}
				else if (data.get(3) == 0x67)
				{
					/*
					 * 升降机位置测试结果
					 */

					if (m_Version1 == 0)
					{
						Hashtable<String, SCState> val = new Hashtable<String, SCState>();

						val.put(String.valueOf((char) ((m_ElevatorLay >> 4) - 0x0A + 'A')),
								m_ElevatorLay >> 4 == data.get(4) >> 4 ? SCState.正常 : SCState.异常);
						m_Info.add(new SCElevatorStatus(val));
					}
					else if (m_Version1 == 1)
					{
						m_ElevatorLocation = String.valueOf((char) (int) data.get(4));
					}
				}
				else if (data.get(3) == 0x68)
				{
					/*
					 * 红外检测
					 */

					int[] val = new int[8];
					int v = data.get(5);

					for (int i = 7; i >= 0; i--)
					{
						val[i] = v & 0x01;
						v = v >> 1;
					}

					m_Info.add(new SCPickStatusStatus(val, m_PickCheck == 0xFF ? SCState.正常 : SCState.无设备));

					if (m_PickTest)
						m_Command.add(new int[] { 0xAA, 0x6A, 0x02, 0x68, 0x00, 0xBB });
				}
				else if (data.get(3) == 0x65)
				{
					/*
					 * 货道状态
					 */

					String lay = Integer.toHexString(data.get(5)).toUpperCase();

					m_State.put(lay, data.get(6) == 0xFF ? SCState.正常
							: (data.get(6) == 0x66 ? SCState.异常 : SCState.无设备));

					if (!m_Init)
					{
						VMC_CargoRoadStatus(lay);

						if (data.get(6) != 0xFF)
						{
							m_Info.add(new SCCargoRoadFault(lay, data.get(6) == 0x66 ? SCLayState.电机不到位
									: SCLayState.无电机));
							m_Error.add(new ErrorData(5, lay + (data.get(6) == 0x66 ? "电机不到位" : "无电机")));
						}
					}

					if (m_LayCheck.size() > 0)
					{
						m_Command.add(new int[] { 0xAA, 0x02 ^ 0x65 ^ m_LayCheck.get(0), 0x02, 0x65, m_LayCheck.get(0),
								0xBB });
						m_LayCheck.remove(0);
					}

					break exit;
				}
				else if (data.get(3) == 0x6B)
				{
					/*
					 * 设置键按下
					 */

					m_Setting = data.get(4) == 0x00;
					m_Error.add(new ErrorData(14, (m_Setting ? "进入" : "退出") + "手动设置状态"));
				}
				else if (data.get(3) == 0x6D)
				{
					/*
					 * 版本号
					 */

					m_Version1 = HexToBCD(data.get(8));
					m_Version2 = HexToBCD(data.get(9));

					m_Info.add(new SCVersion(String.valueOf(m_Version1), String.valueOf(m_Version2)));

					m_Command.add(new int[] { 0xAA, 0x68, 0x03, 0x6E, 0x00, 0x05, 0xBB });
				}
				else if (data.get(3) == 0x6E)
				{
					/*
					 * 机器号
					 */

					m_Info.add(new SCID(ByteToString(data, 4, 7)));
				}

				if (m_AutoCloseDoor)
				{
					if (m_ElevatorLocation.equals("Z"))
					{
						m_ElevatorNumber++;

						if (m_ElevatorNumber >= 10)
						{

							m_ElevatorNumber = 0;
							m_Command.add(new int[] { 0xAA, 0x68, 0x03, 0x6C, 0x00, 0x07, 0xBB });
						}
					}

					if (m_ElevatorLocation.equals("Y"))
					{
						VMC_Elevator("Z");
					}
				}
			}
			catch (Exception ex)
			{

			}
		}

		byte[] temp = new byte[rtn.length];

		for (int i = 0; i < rtn.length; i++)
		{
			temp[i] = (byte) rtn[i];
		}

		return temp;
	}

	/**
	 * 检测指令是否重复
	 * 
	 * @param cmd
	 *            指令
	 * @return 是否重复
	 */
	private boolean CheckCommand(List<Integer> cmd)
	{
		boolean check = false;

		exit:
		{
			if (cmd.size() != m_Cache.size())
			{
				check = true;
				break exit;
			}

			for (int i = 0; i < cmd.size(); i++)
			{
				int a = cmd.get(i);
				int b = m_Cache.get(i);

				if (a != b)
				{
					check = true;
					break exit;
				}
			}
		}

		if (check)
		{
			m_Cache.clear();

			for (int val : cmd)
			{
				m_Cache.add(val);
			}
		}

		return check;
	}

	/**
	 * 销售出货
	 * 
	 * @param key
	 *            货道号
	 * @return 成功
	 */
	public boolean VMC_Sale(String key)
	{
		key = key.toUpperCase();

		if (!(key.length() == 2 && key.charAt(0) >= 'A' && key.charAt(0) <= 'F' && key.charAt(1) >= '0' && key
				.charAt(1) <= '9'))
		{
			m_Error.add(new ErrorData(3, "售货机货道编码错误"));
		}

		int[] val = new int[] { 0xAA, 0x00, 0x02, 0x64, 0x00, 0xBB };

		val[4] = Integer.parseInt(key, 16);
		val[1] = val[2] ^ val[3] ^ val[4];

		m_Command.add(val);

		return true;
	}

	/**
	 * 销售出货
	 * 
	 * @param key
	 *            货道号
	 * @return 成功
	 */
	public boolean VMC_Sale2(String key)
	{
		return VMC_Test("单道", key);
	}

	/**
	 * 售货机状态
	 */
	public void VMC_Status()
	{
		m_Info.add(new SCStatus(CSMachineState.正常, m_MotorQty, m_PickCheck == 0xFF ? SCState.正常 : SCState.无设备,
				m_Elevator ? SCState.正常 : SCState.无设备, m_State, GetCoinsState(), GetNotesState(), m_ElevatorLocation
						.equals("Z"), m_ElevatorLocation));
	}

	/**
	 * 货道状态
	 * 
	 * @param number
	 *            货道号
	 * @return
	 */
	public boolean VMC_CargoRoadStatus(String number)
	{
		number = number.toUpperCase();

		if (number.equals("ALL"))
		{
			m_Info.add(new SCCargoRoadStatus(m_State));
		}
		else
		{
			LinkedHashMap<String, SCState> temp = new LinkedHashMap<String, SCState>();

			if (m_State.containsKey(number))
			{
				temp.put(number, m_State.get(number));
			}

			m_Info.add(new SCCargoRoadStatus(temp));
		}

		return true;
	}

	/**
	 * 纸硬币器状态
	 * 
	 * @return 是否成功
	 */
	public boolean Coin_Status()
	{
		if (m_CoinsRturn != -1)
		{
			return false;
		}

		m_Command.add(new int[] { 0xAA, 0xEB, 0x02, 0x61, 0x88, 0xBB });
		m_Command.add(new int[] { 0xAA, 0xE8, 0x02, 0x62, 0x88, 0xBB });

		return true;
	}

	/**
	 * 升降机位置
	 * 
	 * @return 是否成功
	 */
	public boolean Elevator_Location()
	{
		if (m_Version1 == 1)
		{
			m_Info.add(new SCElevatorLocation(m_ElevatorLocation));
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * 硬币数量
	 * 
	 * @return 是否成功
	 */
	public boolean Coin_Qty()
	{
		m_Command.add(new int[] { 0xAA, 0x35, 0x02, 0x62, 0x55, 0xBB });
		return true;
	}

	/**
	 * 开关纸硬币器
	 * 
	 * @param CFlag
	 *            纸币器:1-收币;0-不收币
	 * @param BFlag
	 *            硬币器:1-收币;0-不收币
	 * @return 是否成功
	 */
	public boolean Coin_Acceptor(int CFlag, int BFlag)
	{
		FileUtil.saveCrashInfo2File("纸币器状态 ＝ " + CFlag + ":时间 ＝ ");
		FileUtil.saveCrashInfo2File("硬币器状态 ＝ " + BFlag + ":时间 ＝ ");
		try
		{
			boolean flag1 = CFlag == 1;
			boolean flag2 = BFlag == 1;

			int val1 = flag1 ? 0xFF : 0x00;
			int val2 = flag2 ? 0xFF : 0x00;

			if (m_CoinsState != 0x88)
			{
				if (flag2)
				{
					m_Command.add(new int[] { 0xAA, 0x9F, 0x02, 0x62, 0xFF, 0xBB });
				}
				else
				{
					m_Command.add(new int[] { 0xAA, 0x60, 0x02, 0x62, 0x00, 0xBB });
				}
			}
			else if (BFlag == 1)
			{
				return false;
			}

			if (m_NotesState != 0x88)
			{
				if (flag1)
				{
					m_Command.add(new int[] { 0xAA, 0x9C, 0x02, 0x61, 0xFF, 0xBB });
				}
				else
				{
					m_Command.add(new int[] { 0xAA, 0x63, 0x02, 0x61, 0x00, 0xBB });
				}
			}
			else if (CFlag == 1)
			{
				return false;
			}

			for (int i = 0; i < 100; i++)
			{
				if ((m_NotesState == 0x88 || m_NotesState == val1) && (m_CoinsState == 0x88 || m_CoinsState == val2))
				{
					return true;
				}

				Thread.sleep(100);
			}

			if (m_NotesState != val1)
			{
				m_Error.add(new ErrorData(12, "纸币器故障"));
			}

			if (m_CoinsState != val2)
			{
				m_Error.add(new ErrorData(13, "硬币器故障"));
			}

			return false;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * 纸硬币器初始化
	 * 
	 * @return 是否成功
	 */
	public boolean Coin_Init()
	{
		try
		{
			m_NotesInit = -1;
			m_CoinsInit = -1;

			m_Command.add(new int[] { 0xAA, 0x6B, 0x02, 0x69, 0x00, 0xBB });
			m_Command.add(new int[] { 0xAA, 0x68, 0x02, 0x6A, 0x00, 0xBB });

			for (int i = 0; i < 100; i++)
			{
				if (m_NotesInit != -1 && m_CoinsInit != -1)
				{
					return m_NotesInit == 1 && m_CoinsInit == 1;
				}

				Thread.sleep(100);
			}
		}
		catch (Exception ex)
		{

		}

		return false;
	}

	/**
	 * 售货机测试
	 * 
	 * @param item
	 *            全道：全货道测试，value="";
	 *            单道：单货道测试，value=货道号（A1-A8、B1-B8、C1-C8、D1-D8、E1-E8、F1-F8）;
	 *            升降：升降平台测试，value=货道号（A-F）; 红外：掉货检测测试，value=""

	 *            参数值
	 * @return
	 */
	public boolean VMC_Test(String item, String value)
	{
		value = value.toUpperCase();

		if (item.equals("全道"))
		{
			m_LayCheck.clear();

			for (int i = 0x0A; i <= 0x0F; i++)
			{
				for (int j = 1; j <= m_Column; j++)
				{
					m_LayCheck.add((byte) ((i << 4) + j % 10));
				}
			}

			m_Command.add(new int[] { 0xAA, 0xC6, 0x02, 0x65, 0xA1, 0xBB });
		}
		else if (item.equals("单道"))
		{
			value = value.toUpperCase();

			if (!(value.length() == 2 && value.charAt(0) >= 'A' && value.charAt(0) <= 'F' && value.charAt(1) >= '0' && value
					.charAt(1) <= '9'))
			{
				m_Error.add(new ErrorData(3, "售货机货道编码错误"));
			}

			int[] temp = new int[] { 0xAA, 0x00, 0x02, 0x65, 0x00, 0xBB };

			temp[4] = Integer.parseInt(value, 16);
			temp[1] = temp[2] ^ temp[3] ^ temp[4];

			m_Command.add(temp);
		}
		else if (item.equals("升降"))
		{
			if (m_Version1 == 0)
			{
				if (value.length() == 1 && value.charAt(0) >= 'A' && value.charAt(0) <= 'F')
				{
					int[] temp = new int[] { 0xAA, 0x00, 0x02, 0x66, 0x00, 0xBB };

					temp[4] = m_ElevatorLay = Integer.parseInt(value + "1", 16);
					temp[1] = temp[2] ^ temp[3] ^ temp[4];

					m_Command.add(temp);
				}
			}
			else if (m_Version1 == 1)
			{
				VMC_Elevator(value);
			}
		}
		else if (item.equals("红外"))
		{
			m_PickTest = true;
			m_Command.add(new int[] { 0xAA, 0x6A, 0x02, 0x68, 0x00, 0xBB });
		}

		return true;
	}

	/**
	 * 升降机运行
	 * 
	 * @param value
	 *            层号
	 */
	public void VMC_Elevator(String value)
	{
		if (m_Version1 != 0)
		{
			if (m_ElevatorRun.containsKey(value))
			{
				m_Command.add(m_ElevatorRun.get(value));
			}
		}
	}

	/**
	 * 退出红外测试
	 */
	public void VMC_ExitTest()
	{
		m_PickTest = false;
	}

	/**
	 * 纸币确认收币
	 * 
	 * @param state
	 *            是否收币
	 * @return
	 */
	public boolean Coin_Confirm(boolean state)
	{
		int[] val = new int[] { 0xAA, 0x05, 0x02, 0x61, state ? 0x99 : 0x66, 0xBB };
		val[1] = val[2] ^ val[3] ^ val[4];

		m_Command.add(val);

		return true;
	}

	/**
	 * 硬币器退币
	 * 
	 * @param amount
	 *            金额
	 * @return
	 */
	public boolean Coin_Return(int amount)
	{
		m_Amount = amount;
		m_CoinsRturn = 0;

		m_Command.add(new int[] { 0xAA, 0xE8, 0x02, 0x62, 0x88, 0xBB });

		return true;
	}

	private SCState GetCoinsState()
	{
		return m_CoinsState == 0x88 ? SCState.无设备 : (m_CoinsState == 0x77 ? SCState.异常 : SCState.正常);
	}

	private SCState GetNotesState()
	{
		return m_NotesState == 0x88 ? SCState.无设备 : (m_NotesState == 0x77 ? SCState.异常 : SCState.正常);
	}

	private int HexToBCD(int val)
	{
		return (val >> 4) * 10 + (val & 0xF);
	}

	private int[] BCDToHex(int val, int lenght)
	{
		int[] rtn = new int[lenght];

		for (int i = lenght - 1; i >= 0; i--)
		{
			int temp = val % 100;
			val /= 100;
			rtn[i] = ((temp / 10) << 4) + temp % 10;
		}

		return rtn;
	}

	private String ByteToString(List<Integer> val, int start, int length)
	{
		String rtn = "";

		for (int i = start; i < start + length; i++)
		{
			int v = val.get(i);

			rtn += (v > 15 ? "0" : "") + Integer.toHexString(v).toUpperCase();
		}

		return rtn;
	}

	private class InfoTask extends TimerTask
	{
		private boolean m_Event = false;
		private StatusCapture m_Data;

		public InfoTask(StatusCapture data)
		{
			m_Data = data;
		}

		@Override
		public void run()
		{
			if (!m_Event)
			{
				m_Event = true;

				try
				{
					while (m_Info.size() > 0)
					{
						if (m_Data.m_EventHandler != null)
						{
							//TODO
							m_Data.m_EventHandler.InfoEventHandler(m_Data.m_Info.get(0).packageCode,
									m_Data.m_Info.get(0));
						}
						m_Info.remove(0);
					}

					while (m_Error.size() > 0)
					{
						if (m_Data.m_EventHandler != null)
						{
							m_Data.m_EventHandler.ErrorEventHandler((int) m_Data.m_Error.get(0).ID,
									m_Data.m_Error.get(0).Info);
						}
						m_Error.remove(0);
					}
				}
				catch (Exception ex)
				{

				}

				m_Event = false;
			}
		}
	}

	private class HeartTask extends TimerTask
	{
		private StatusCapture m_Data;

		public HeartTask(StatusCapture data)
		{
			m_Data = data;
		}

		@Override
		public void run()
		{
			m_CoinsState = -1;
			m_NotesState = -1;

			m_Data.m_Info.add(new SCHeartbeat());
		}
	}

	private class ErrorData
	{
		int ID;
		String Info;

		ErrorData(int id, String info)
		{
			ID = id;
			Info = info;
		}
	}
	public int coinStatus(){
		return m_CoinsState;
	}
	public int notesStatus(){
		return m_NotesState;
	}
	/**
	 * 开关纸币器
	 * 
	 * @param CFlag
	 *            纸币器:1-收币;0-不收币
	 * @return 是否成功
	 */
	public boolean Note_Acceptor(int CFlag)
	{
		try
		{
			boolean flag = CFlag == 1;
			int val = flag ? 0xFF : 0x00;

			if (m_NotesState != 0x88)
			{
				if (m_NotesState != val)
				{
					if (flag)
					{
						m_Command.add(new int[] { 0xAA, 0x9C, 0x02, 0x61, 0xFF, 0xBB });
					}
					else
					{
						m_Command.add(new int[] { 0xAA, 0x63, 0x02, 0x61, 0x00, 0xBB });
					}
				}
			}
			else if (CFlag == 1)
			{
				return false;
			}

			for (int i = 0; i < 100; i++)
			{
				if (m_NotesState == 0x88 || m_NotesState == val)
				{
					return true;
				}

				Thread.sleep(100);
			}

			if (m_NotesState != val)
			{
				m_Error.add(new ErrorData(12, "纸币器故障"));
			}

			return false;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * 开关纸硬币器
	 * 
	 * @param BFlag
	 *            硬币器:1-收币;0-不收币
	 * @return 是否成功
	 */
	public boolean Coin_Acceptor(int BFlag)
	{
		try
		{
			boolean flag = BFlag == 1;
			int val = flag ? 0xFF : 0x00;

			if (m_CoinsState != 0x88)
			{
				if (m_CoinsState != val)
				{
					if (flag)
					{
						m_Command.add(new int[] { 0xAA, 0x9F, 0x02, 0x62, 0xFF, 0xBB });
					}
					else
					{
						m_Command.add(new int[] { 0xAA, 0x60, 0x02, 0x62, 0x00, 0xBB });
					}
				}
			}
			else if (BFlag == 1)
			{
				return false;
			}

			for (int i = 0; i < 100; i++)
			{
				if (m_CoinsState == 0x88 || m_CoinsState == val)
				{
					return true;
				}

				Thread.sleep(100);
			}

			if (m_CoinsState != val)
			{
				m_Error.add(new ErrorData(13, "硬币器故障"));
			}

			return false;
		}
		catch (Exception ex)
		{
			return false;
		}
	}
}
