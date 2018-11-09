package com.leiyuntop.Refrigerator;

import java.io.File;
import java.io.InputStream;
import java.util.*;

import android_serialport_api.SerialPort;

/**
 * 制冷机组管理
 * 
 * @author MENGQINGYU
 * 
 */
public class Refrigerator implements Runnable
{
	private SerialPort m_Port;
	private String m_PortName;
	private boolean m_Exit;

	private LinkedHashMap<ECommand, int[]> m_LimitValue = new LinkedHashMap<ECommand, int[]>();

	private double[] m_Temp = new double[4];
	private ERunModel m_RunModel;
	private ERunState m_RunState;
	private EAlarm m_Alarm;
	private EUnitState m_UnitState;
	private int m_PowerProtectionTime;
	private int m_EvaporatorFanOffDelayTime;
	private int m_RefrigerationTemp;
	private int m_RefrigerationBacklash;
	private int m_DefrostTime;
	private int m_DefrostInterval;
	private int m_CondenserFanStartTemp;
	private int m_CondenserTempAlarmTemp;
	private int m_HardwareVersion;
	private int m_InterfaceVersion;
	private int m_HeatTemp;
	private int m_HeatBacklash;

	private Timer timerHeart = new Timer();
	private Timer timerSend = new Timer();

	private Byte[] m_ReceiveString; // 接收数据
	private EReceiveState m_ReceiveState = EReceiveState.完成; // 接收状态

	private IRefrigeratorData m_LastData = null; // 上次发送
	private List<IRefrigeratorData> m_SendList = new ArrayList<IRefrigeratorData>(); // 发送缓存

	private int m_Number = 0; // 接收时间计数

	/**
	 * 温度
	 */
	public double[] getTemp()
	{
		return m_Temp;
	}

	/**
	 * 字段最大值和最小值数据
	 * 
	 * @return 值
	 */
	public LinkedHashMap<ECommand, int[]> getLimitValue()
	{
		return m_LimitValue;
	}

	/**
	 * 读运行模式
	 * 
	 * @return 值
	 */
	public ERunModel getRunModel()
	{
		return m_RunModel;
	}

	/**
	 * 写运行模式
	 * 
	 * @param model
	 *            值
	 */
	public void setRunMode(ERunModel model)
	{
		m_SendList.add(new 写机组运行模式(model));
		m_SendList.add(new 读运行状态());
	}

	/**
	 * 读运行状态
	 * 
	 * @return 值
	 */
	public ERunState getRunState()
	{
		return m_RunState;
	}

	/**
	 * 读报警
	 * 
	 * @return 值
	 */
	public EAlarm getAlarm()
	{
		return m_Alarm;
	}

	/**
	 * 读设备状态
	 * 
	 * @return 值
	 */
	public EUnitState getUnitState()
	{
		return m_UnitState;
	}

	/**
	 * 读开机保护时间
	 * 
	 * @return 值
	 */
	public int getPowerProtectionTime()
	{
		return m_PowerProtectionTime;
	}

	/**
	 * 写开机保护时间
	 * 
	 * @param value
	 *            值
	 */
	public void setPowerProtectionTime(int value)
	{
		m_SendList.add(new 写开机保护时间(value));
		m_SendList.add(new 读配置(this));
	}

	/**
	 * 读蒸发器风扇延时关时间
	 * 
	 * @return 值
	 */
	public int getEvaporatorFanOffDelayTime()
	{
		return m_EvaporatorFanOffDelayTime;
	}

	/**
	 * 写蒸发器风扇延时关时间
	 * 
	 * @param value
	 *            值
	 */
	public void setEvaporatorFanOffDelayTime(int value)
	{
		m_SendList.add(new 写蒸发器风扇延时关时间(value));
		m_SendList.add(new 读配置(this));
	}

	/**
	 * 读制冷控制温度
	 * 
	 * @return 值
	 */
	public int getRefrigerationTemp()
	{
		return m_RefrigerationTemp;
	}

	/**
	 * 写制冷控制温度
	 * 
	 * @param value
	 *            值
	 */
	public void setRefrigerationTemp(int value)
	{
		m_SendList.add(new 写制冷控制温度(value, this));
		m_SendList.add(new 读配置(this));
	}

	/**
	 * 读制冷控制回差
	 * 
	 * @return 值
	 */
	public int getRefrigerationBacklash()
	{
		return m_RefrigerationBacklash;
	}

	/**
	 * 写制冷控制回差
	 * 
	 * @param value
	 *            值
	 */
	public void setRefrigerationBacklash(int value)
	{
		m_SendList.add(new 写制冷控制回差(value));
		m_SendList.add(new 读配置(this));
	}

	/**
	 * 读化霜时间
	 * 
	 * @return 值
	 */
	public int getDefrostTime()
	{
		return m_DefrostTime;
	}

	/**
	 * 写化霜时间
	 * 
	 * @return 值
	 */
	public void setDefrostTime(int value)
	{
		m_SendList.add(new 写化霜时间(value));
		m_SendList.add(new 读配置(this));
	}

	/**
	 * 读化霜间隔
	 * 
	 * @return 值
	 */
	public int getDefrostInterval()
	{
		return m_DefrostInterval;
	}

	/**
	 * 写化霜间隔
	 * 
	 * @param value
	 *            值
	 */
	public void setDefrostInterval(int value)
	{
		m_SendList.add(new 写化霜间隔(value));
		m_SendList.add(new 读配置(this));
	}

	/**
	 * 读冷凝器风扇启动温度
	 * 
	 * @return 值
	 */
	public int getCondenserFanStartTemp()
	{
		return m_CondenserFanStartTemp;
	}

	/**
	 * 写冷凝器风扇启动温度
	 * 
	 * @param value
	 *            值
	 */
	public void setCondenserFanStartTemp(int value)
	{
		m_SendList.add(new 写冷凝器风扇启动温度(value, this));
		m_SendList.add(new 读配置(this));
	}

	/**
	 * 读冷凝器高温报警温度
	 * 
	 * @return 值
	 */
	public int getCondenserTempAlarmTemp()
	{
		return m_CondenserTempAlarmTemp;
	}

	/**
	 * 写冷凝器高温报警温度
	 * 
	 * @param value
	 *            值
	 */
	public void setCondenserTempAlarmTemp(int value)
	{
		m_SendList.add(new 写冷凝器高温报警温度(value, this));
		m_SendList.add(new 读配置(this));
	}

	/**
	 * 读加热控制温度
	 * 
	 * @return 值
	 */
	public int getHeatTemp()
	{
		return m_HeatTemp;
	}

	/**
	 * 写加热控制温度
	 * 
	 * @param value
	 *            值
	 */
	public void setHeatTemp(int value)
	{
		m_SendList.add(new 写加热控制温度(value));
		m_SendList.add(new 读配置(this));
	}

	/**
	 * 读加热控制回差
	 * 
	 * @return 值
	 */
	public int getHeatBacklash()
	{
		return m_HeatBacklash;
	}

	/**
	 * 写加热控制回差
	 * 
	 * @param value
	 *            值
	 */
	public void setHeatBacklash(int value)
	{
		m_SendList.add(new 写加热控制回差(value));
		m_SendList.add(new 读配置(this));
	}

	/**
	 * 版本号
	 * 
	 * @return 值
	 */
	public int getHardwareVersion()
	{
		return m_HardwareVersion;
	}

	/**
	 * 通讯版本
	 * 
	 * @return 值
	 */
	public int getInterfaceVersion()
	{
		return m_InterfaceVersion;
	}

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
	 * @return 是否成功
	 */
	public boolean Start(String portName)
	{
		m_PortName = portName;
		return Start();
	}

	private boolean Start()
	{
		try
		{
			m_Port = new SerialPort(new File(m_PortName), 9600, 0);

			timerSend.schedule(new SendTask(), 100, 100);
			timerHeart.schedule(new HeartTask(this), 10000, 10000);

			m_Exit = false;

			Thread thread = new Thread(this);
			thread.start();

			m_SendList.add(new 读版本号());
			m_SendList.add(new 读机组运行模式());
			m_SendList.add(new 读开机保护时间());
			m_SendList.add(new 读蒸发器风扇延时关时间());
			m_SendList.add(new 读制冷控制温度(this));
			m_SendList.add(new 读制冷控制回差());
			m_SendList.add(new 读化霜时间());
			m_SendList.add(new 读化霜间隔());
			m_SendList.add(new 读冷凝器风扇启动温度(this));
			m_SendList.add(new 读冷凝器高温报警温度(this));
			m_SendList.add(new 读加热控制温度());
			m_SendList.add(new 读加热控制回差());

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
		timerHeart.cancel();
		timerSend.cancel();

		m_Exit = true;
		m_Port.close();
	}

	private void ErrorData()
	{
		m_LastData = null;
		m_SendList.clear();

		Close();
		Start();
	}

	@Override
	public void run()
	{
		List<Byte> data = new ArrayList<Byte>();
		int val = 0;
		int len = 200;
		int index = 0;

		InputStream inputStream = m_Port.getInputStream(); // 从串口来的输入流

		while (!m_Exit)
		{
			try
			{
				if (inputStream.available() > 0)
				{
					val = inputStream.read();

					if (val == 0xAA && data.size() == 0)
					{
						data.clear();
						len = 200;
						index = 0;
					}

					if (val == 0xAA || data.size() != 0)
					{
						data.add((byte) val);
						index++;
					}

					if (index == 2)
					{
						len = val;
					}

					if (val == 0xBB)
					{
						if (val == 0xBB && index >= len)
						{
							ExecCommand(data);

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

	private void ExecCommand(List<Byte> data)
	{
		try
		{
			Byte[] val = new Byte[data.size()];
			data.toArray(val);

			if (val.length < 5)
			{
				return;
			}

			if (!(val[0] == (byte) 0xAA && val[val.length - 1] == (byte) 0xBB))
			{
				return;
			}

			int check = 0;

			for (int i = 0; i < val.length; i++)
			{
				check ^= val[i];
			}

			if (check != 0)
			{
				return;
			}

			m_ReceiveString = val;
			m_ReceiveState = EReceiveState.完成;
		}
		catch (Exception ex)
		{

		}
	}

	private class SendTask extends TimerTask
	{
		@Override
		public void run()
		{
			try
			{
				if (m_LastData == null || m_LastData != null && m_ReceiveState == EReceiveState.完成
						&& m_ReceiveString != null)
				{
					if (m_LastData != null)
					{
						try
						{
							m_LastData.GetReceive(m_ReceiveString);

							ECommand key = m_LastData.getKey();

							if (key == ECommand.读温度)
							{
								读温度 obj = (读温度) m_LastData;

								m_Temp = obj.温度;
							}
							else if (key == ECommand.读运行状态)
							{
								读运行状态 obj = (读运行状态) m_LastData;

								m_RunModel = obj.运行模式;
								m_RunState = obj.运行状态;
								m_Alarm = obj.报警;
							}
							else if (key == ECommand.读设备状态)
							{
								读设备状态 obj = (读设备状态) m_LastData;
								m_UnitState = obj.设备状态;
							}
							else if (key == ECommand.读配置)
							{
								读配置 obj = (读配置) m_LastData;

								m_PowerProtectionTime = obj.开机保护时间;
								m_EvaporatorFanOffDelayTime = obj.蒸发器风扇延时关时间;
								m_RefrigerationTemp = obj.制冷控制温度;
								m_RefrigerationBacklash = obj.制冷控制回差;
								m_DefrostTime = obj.化霜时间;
								m_DefrostInterval = obj.化霜间隔;
								m_CondenserFanStartTemp = obj.冷凝器风扇启动温度;
								m_CondenserTempAlarmTemp = obj.冷凝器高温报警温度;
								m_HeatTemp = obj.加热控制温度;
								m_HeatBacklash = obj.加热控制回差;
							}
							else if (key == ECommand.读机组运行模式)
							{
								读机组运行模式 obj = (读机组运行模式) m_LastData;

								m_LimitValue.put(ECommand.写机组运行模式, new int[] { obj.最小值, obj.最大值 });
								m_RunModel = obj.运行模式;
							}
							else if (key == ECommand.读开机保护时间)
							{
								读开机保护时间 obj = (读开机保护时间) m_LastData;

								m_LimitValue.put(ECommand.写开机保护时间, new int[] { obj.最小值, obj.最大值 });
								m_PowerProtectionTime = obj.开机保护时间;
							}
							else if (key == ECommand.读蒸发器风扇延时关时间)
							{
								读蒸发器风扇延时关时间 obj = (读蒸发器风扇延时关时间) m_LastData;

								m_LimitValue.put(ECommand.写蒸发器风扇延时关时间, new int[] { obj.最小值, obj.最大值 });
								m_EvaporatorFanOffDelayTime = obj.蒸发器风扇延时关时间;
							}
							else if (key == ECommand.读制冷控制温度)
							{
								读制冷控制温度 obj = (读制冷控制温度) m_LastData;

								m_LimitValue.put(ECommand.写制冷控制温度, new int[] { obj.最小值, obj.最大值 });
								m_RefrigerationTemp = obj.制冷控制温度;
							}
							else if (key == ECommand.读制冷控制回差)
							{
								读制冷控制回差 obj = (读制冷控制回差) m_LastData;

								m_LimitValue.put(ECommand.写制冷控制回差, new int[] { obj.最小值, obj.最大值 });
								m_RefrigerationBacklash = obj.制冷控制回差;
							}
							else if (key == ECommand.读化霜时间)
							{
								读化霜时间 obj = (读化霜时间) m_LastData;

								m_LimitValue.put(ECommand.写化霜时间, new int[] { obj.最小值, obj.最大值 });
								m_DefrostTime = obj.化霜时间;
							}
							else if (key == ECommand.读化霜间隔)
							{
								读化霜间隔 obj = (读化霜间隔) m_LastData;

								m_LimitValue.put(ECommand.写化霜间隔, new int[] { obj.最小值, obj.最大值 });
								m_DefrostInterval = obj.化霜间隔;
							}
							else if (key == ECommand.读冷凝器风扇启动温度)
							{
								读冷凝器风扇启动温度 obj = (读冷凝器风扇启动温度) m_LastData;

								m_LimitValue.put(ECommand.写冷凝器风扇启动温度, new int[] { obj.最小值, obj.最大值 });
								m_CondenserFanStartTemp = obj.冷凝器风扇启动温度;
							}
							else if (key == ECommand.读冷凝器高温报警温度)
							{
								读冷凝器高温报警温度 obj = (读冷凝器高温报警温度) m_LastData;

								m_LimitValue.put(ECommand.写冷凝器高温报警温度, new int[] { obj.最小值, obj.最大值 });
								m_CondenserTempAlarmTemp = obj.冷凝器高温报警温度;
							}
							else if (key == ECommand.读加热控制温度)
							{
								读加热控制温度 obj = (读加热控制温度) m_LastData;

								m_LimitValue.put(ECommand.写加热控制温度, new int[] { obj.最小值, obj.最大值 });
								m_HeatTemp = obj.加热控制温度;
							}
							else if (key == ECommand.读加热控制回差)
							{
								读加热控制回差 obj = (读加热控制回差) m_LastData;

								m_LimitValue.put(ECommand.写加热控制回差, new int[] { obj.最小值, obj.最大值 });
								m_HeatBacklash = obj.加热控制回差;
							}
							else if (key == ECommand.读版本号)
							{
								读版本号 obj = (读版本号) m_LastData;

								m_HardwareVersion = obj.硬件版本;
								m_InterfaceVersion = obj.接口版本;
							}
						}
						catch (Exception ex)
						{

						}

						m_LastData = null;
						m_ReceiveString = null;
						m_ReceiveState = EReceiveState.完成;
					}

					m_Number = 0;

					if (m_SendList.size() > 0)
					{
						Thread.sleep(10);

						m_LastData = m_SendList.get(0);
						m_SendList.remove(0);
						m_ReceiveState = EReceiveState.等待中;

						byte[] send = m_LastData.GetSend();

						m_Port.getOutputStream().write(send);
					}
				}
				else
				{
					m_Number++;

					if (m_Number > 50)
					{
						ErrorData();
					}
				}
			}
			catch (Exception ex)
			{
				m_SendList.clear();
			}
		}
	}

	private class HeartTask extends TimerTask
	{
		private Refrigerator m_Data;

		public HeartTask(Refrigerator data)
		{
			m_Data = data;
		}

		@Override
		public void run()
		{
			if (m_SendList.size() < 10)
			{
				m_SendList.add(new 读温度());
				m_SendList.add(new 读运行状态());
				m_SendList.add(new 读设备状态());
				m_SendList.add(new 读配置(m_Data));
			}
		}
	}
}