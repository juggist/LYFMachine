// **********************************************************************
//
// Copyright (c) 2003-2013 ZeroC, Inc. All rights reserved.
//
// This copy of Chat Demo is licensed to you under the terms described
// in the CHAT_DEMO_LICENSE file included in this distribution.
//
// **********************************************************************

package com.icoffice.library.ice;

import java.util.HashMap;
import java.util.Map;

import Glacier2.SessionHelper;
import Glacier2.SessionNotExistException;
import android.os.Handler;
import android.os.Message;

import com.icoffice.library.handler.BaseHttpHandler;
import com.icoffice.library.moudle.control.BaseMachineControl;
import com.icoffice.library.moudle.control.UpgradeControl.ResultType;
import com.icoffice.library.utils.CommonUtils;

public class Coordinator
{
    public enum ClientState { Disconnected, Connecting, Connected, Disconnecting };

    public Coordinator(BaseMachineControl machineControl, String[] args)
    {
    	_machineControl = machineControl;
        _args = args;

        Ice.InitializationData initData = new Ice.InitializationData();
        initData.properties = Ice.Util.createProperties(new Ice.StringSeqHolder(_args));
        initData.properties.setProperty("Ice.Plugin.IceSSL", "IceSSL.PluginFactory");
        initData.properties.setProperty("IceSSL.VerifyPeer", "0");
        initData.properties.setProperty("Ice.Override.Timeout", "60000");
        initData.properties.setProperty("Ice.Override.ConnectTimeout", "3000");
        initData.dispatcher = new Ice.Dispatcher()
            {
                public void
                dispatch(Runnable runnable, Ice.Connection connection)
                {
                    runnable.run();
                }
            };

        final Coordinator coordinator = this;
        _factory = new Glacier2.SessionFactoryHelper(initData, new Glacier2.SessionCallback()
            {
                public void
                connected(final SessionHelper session)
                    throws SessionNotExistException
                {
                    //
                    // Ignore callbacks during shutdown.
                    //
                    if(_exit)
                    {
                        return;
                    }

                    //
                    // If the session has been reassigned avoid the spurious callback.
                    //
                    
                    if(session != _session)
                    {
                        return;
                    }

                    CofficeServer.MachineCallbackPrx callback = CofficeServer.MachineCallbackPrxHelper.uncheckedCast(
                                                        _session.addWithUUID(new MachineCallbackI(_machineControl)));

                    _machine = CofficeServer.MachineSessionPrxHelper.uncheckedCast(_session.session());
                    try
                    {
                        _machine.begin_setCallback(callback, new CofficeServer.Callback_MachineSession_setCallback()
                            {
                                @Override
                                public void
                                response()
                                {
                                    setState(ClientState.Connected);
                                }

                                @Override
                                public void
                                exception(Ice.LocalException ex)
                                {
                                    destroySession();
                                }
                            });
                    }
                    catch(Ice.CommunicatorDestroyedException ex)
                    {
                        //Ignore client session was destroyed.
                    }
                    
            		Message msg = new Message();
					msg.what = BaseHttpHandler.ICE_CONNECTED_SUCCESS;
					_machineControl.mHttpHandler.sendMessage(msg);
                }

                public void
                disconnected(SessionHelper session)
                {
                    //
                    // Ignore callbacks during shutdown.
                    //
                    if(_exit)
                    {
                        return;
                    }

                    _username = "";
                    if(_state == ClientState.Disconnecting) // Connection closed by user logout/exit
                    {
                        setState(ClientState.Disconnected);
                    }
                    else if(_state == ClientState.Connected) // Connection lost while user was chatting
                    {
                        setError("<system-message> - The connection with the server was unexpectedly lost.\n" +
                                 "Try to login again.");
                    }
                    else // Connection lost while user was connecting
                    {
                        setError("<system-message> - The connection with the server was unexpectedly lost.\n" +
                                 "Try again.");
                    }
                }

                public void
                connectFailed(SessionHelper session, Throwable exception)
                {
                    //
                    // Ignore callbacks during shutdown.
                    //
                    if(_exit)
                    {
                        return;
                    }

                    try
                    {
                        throw exception;
                    }
                    catch(final Glacier2.CannotCreateSessionException ex)
                    {
                        setError("Login failed (Glacier2.CannotCreateSessionException):\n" + ex.reason);
                    }
                    catch(final Glacier2.PermissionDeniedException ex)
                    {
                        setError("Login failed (Glacier2.PermissionDeniedException):\n" + ex.reason);
                    }
                    catch(Ice.ConnectionRefusedException ex)
                    {
                        setError("Login failed (Ice.ConnectionRefusedException).\n" +
                                 "Please check your server:\n" +
                                 "a Glacier2 router should be running on " + _host + " and\n" +
                                 "listening on port 4064, and the server firewall needs\n" +
                                 "to be configured to allow TCP connections to this port.");
                    }
                    catch(Ice.TimeoutException ex)
                    {
                        setError("Login failed (Ice.TimeoutException)\n." +
                                 "Please check your server:\n" +
                                 "a Glacier2 router should be running on " + _host + " and\n" +
                                 "listening on port 4064, and the server firewall needs\n" +
                                 "to be configured to allow TCP connections to this port.");
                    }
                    catch(Ice.SocketException ex)
                    {
                        setError("Login failed (Ice.SocketException ).\n" +
                                 "Please check your server:\n" +
                                 "a Glacier2 router should be running on " + _host + " and\n" +
                                 "listening on port 4064, and the server firewall needs\n" +
                                 "to be configured to allow TCP connections to this port.");
                    }
                    catch(Ice.DNSException ex)
                    {
                        setError("Login failed (Ice.DNSException ).\n" +
                                 "Please check your DNS configuration:\n" +
                                 "Host \"" + _host + "\" should point to the ip address of a Glacier2 router\n" +
                                 "that should be listening on port 4064, and the server firewall needs\n" +
                                 "to be configured to allow TCP connections to this port.");
                    }
                    catch(final Throwable ex)
                    {
                        setError("Login failed:\n" + ex.toString());
                    }

                }
    
                public void
                createdCommunicator(SessionHelper session)
                {
                }
            });
    }

    public void login(String username, String password, String host)
    {
        setState(ClientState.Connecting);
        _username = username;
        _host = host;
        _factory.setRouterHost(_host);
        _session = _factory.connect(_username, password);
    }

    public void logout()
    {
        setState(ClientState.Disconnecting);
        destroySession();
    }

    public void exit()
    {
        _exit = true;
        Ice.Communicator communicator = _session == null ? null : _session.communicator();
        destroySession();
        if(communicator != null)
        {
            communicator.waitForShutdown();
        }
        System.exit(0);
    }

    public void setState(ClientState state)
    {
        _state = state;
        if(state == ClientState.Disconnected)
        {
        }
        else if(state == ClientState.Connecting)
        {
        }
        else if(state == ClientState.Connected)
        {
        }
        else if(state == ClientState.Disconnecting)
        {
        }
    }

    public ClientState getState()
    {
        return _state;
    }

    public void setError(final String message)
    {
        //
        // Don't display errors at that point GUI is being destroyed.
        //
        if(_exit == true)
        {
            return;
        }
        if(_state != ClientState.Connected)
        {
            setState(ClientState.Disconnected);
            CommonUtils.showLog(CommonUtils.ICE_TAG,message);
        }
        else
        {
        	CommonUtils.showLog(CommonUtils.ICE_TAG,message);
        }
    }

    public String getUsername()
    {
        return _username;
    }

    public boolean downloadApk(Map<String, String> options, Handler handler)
    {
        if(_machine != null)
        {
            try
            {
            	CommonUtils.showLog(CommonUtils.ICE_TAG,"begin_downloadApk");
                _machine.begin_downloadApk(options, new AMI_MachineSession_downloadApkI(handler));
                return true;
            }
            catch(Ice.CommunicatorDestroyedException ex)
            {
                //Ignore client session was destroyed.
            	return false;
            }
        }
        return false;
    }

    public void heartBeat(Map<String, String> options)
    {
        if(_machine != null)
        {
            try
            {
                _machine.begin_heartBeat(options, new CofficeServer.Callback_MachineSession_heartBeat() {

            		@Override
            		public void response(Map<String, String> __ret) {
            			// TODO Auto-generated method stub
            			CommonUtils.showLog(CommonUtils.ICE_TAG,"heartBeat: "+__ret);
            		}
            		
                    public void exception(Ice.LocalException ex)
                    {
                        destroySession();
                    }

                    public void exception(Ice.UserException ex)
                    {
                    	CommonUtils.showLog(CommonUtils.ICE_TAG,"heartBeat exception: "+ex.toString());
                    }

                });
            }
            catch(Ice.CommunicatorDestroyedException ex)
            {
                //Ignore client session was destroyed.
            }
        }
    }


    public void queryMachineGoods(Map<String, String> options)
    {
        if(_machine != null)
        {
            try
            {
                _machine.begin_queryMachineGoods(options, new CofficeServer.Callback_MachineSession_queryMachineGoods() {

            		@Override
            		public void response(Map<String, String> __ret) {
            			// TODO Auto-generated method stub
            			CommonUtils.showLog(CommonUtils.ICE_TAG,"queryMachineGoods: "+__ret);
                        
                		Message msg = new Message();
    					msg.what = BaseHttpHandler.ICE_GET_GOODSBEAN_SUCCESS;
						msg.obj = __ret;
    					_machineControl.mHttpHandler.sendMessage(msg);
            		}
            		
                    public void exception(Ice.LocalException ex)
                    {
                        destroySession();
                    }

                    public void exception(Ice.UserException ex)
                    {
                    	CommonUtils.showLog(CommonUtils.ICE_TAG,"heartBeat exception: "+ex.toString());
                    }

                });
            }
            catch(Ice.CommunicatorDestroyedException ex)
            {
                //Ignore client session was destroyed.
            }
        }
    }
    public void queryMachineAd(Map<String, String> options)
    {
        if(_machine != null)
        {
            try
            {
                _machine.begin_queryMachineAd(options, new CofficeServer.Callback_MachineSession_queryMachineAd() {

            		@Override
            		public void response(Map<String, String> __ret) {
            			// TODO Auto-generated method stub
            			CommonUtils.showLog(CommonUtils.ICE_TAG,"queryMachineAd: "+__ret);
                        
                		Message msg = new Message();
    					msg.what = BaseHttpHandler.ICE_QUERYMACHINEAD_SUCCESS;
						msg.obj = __ret;
    					_machineControl.mHttpHandler.sendMessage(msg);
            		}
            		
                    public void exception(Ice.LocalException ex)
                    {
                        destroySession();
                    }

                    public void exception(Ice.UserException ex)
                    {
                    	CommonUtils.showLog(CommonUtils.ICE_TAG,"heartBeat exception: "+ex.toString());
                    }

                });
            }
            catch(Ice.CommunicatorDestroyedException ex)
            {
                //Ignore client session was destroyed.
            }
        }
    }

    
    public Map<String, String> getConfig(String machineCode)
    {
        if(_machine != null)
        {
            try
            {
            	Map<String, String> info = _machine.getConfig(machineCode);
                return info;
            }
            catch(Ice.CommunicatorDestroyedException ex)
            {
                //Ignore client session was destroyed.
            	ex.printStackTrace();
            }
            catch(Exception e){
            	e.printStackTrace();
            }
        }
        return new HashMap<String, String>();
    }

    public Map<String, String> createOrderCofficeCard(Map<String, String> options)
    {
        if(_machine != null)
        {
            try
            {
                return _machine.createOrderCofficeCard(options);
            }
            catch(Ice.CommunicatorDestroyedException ex)
            {
                //Ignore client session was destroyed.
            }
        }
        return new HashMap<String, String>();
    }

    //
    // Callback for the downloadApk async operation.
    //
    public class AMI_MachineSession_downloadApkI extends CofficeServer.Callback_MachineSession_downloadApk
    {
        public AMI_MachineSession_downloadApkI(Handler handler)
        {
            _handler = handler;
        }

		@Override
		public void response(Map<String, String> __ret) {
			// TODO Auto-generated method stub
			CommonUtils.showLog(CommonUtils.ICE_TAG,"downloadApk: "+__ret);
            Message msg = new Message();
            msg.what = ResultType.Success.ordinal();
            msg.obj = __ret;
            _handler.sendMessage(msg);
		}
		
        public void exception(Ice.LocalException ex)
        {
            Message msg = new Message();
            msg.what = ResultType.Fail.ordinal();
            msg.obj = ex.toString();
            _handler.sendMessage(msg);
            destroySession();
        }

        private final Handler _handler;
    }
    
    public void checkWeixinOrderState(Map<String, String> options)
    {
    	CommonUtils.showLog(CommonUtils.ICE_TAG,"ICE weChat startTime = " + CommonUtils.currentTime());
        if(_machine != null)
        {
            try
            {
                _machine.begin_checkWeixinOrderState(options, new CofficeServer.Callback_MachineSession_checkWeixinOrderState() {

            		@Override
            		public void response(Map<String, String> __ret) {
            			// TODO Auto-generated method stub
            			CommonUtils.showLog(CommonUtils.ICE_TAG,"checkWeixinOrderState: "+__ret);
            			CommonUtils.showLog(CommonUtils.ICE_TAG,"ICE weChat success = " + CommonUtils.currentTime());
                		Message msg = new Message();
    					msg.what = BaseHttpHandler.ICE_ROLL_WX_SUCCESS;
						msg.obj = __ret;
    					_machineControl.mHttpHandler.sendMessage(msg);
            		}
            		
                    public void exception(Ice.LocalException ex)
                    {
                    	CommonUtils.showLog(CommonUtils.ICE_TAG,"ICE weChat endTime = " + CommonUtils.currentTime());
                        destroySession();
                    }

                    public void exception(Ice.UserException ex)
                    {
                    	CommonUtils.showLog(CommonUtils.ICE_TAG,"checkWeixinOrderState exception: "+ex.toString());
                    }

                });
            }
            catch(Ice.CommunicatorDestroyedException ex)
            {
                //Ignore client session was destroyed.
            }
        }
    }
    public void createOrderCash(Map<String, String>[] options)
    {
        if(_machine != null)
        {
            try
            {
                _machine.begin_createOrderCash(options, new CofficeServer.Callback_MachineSession_createOrderCash() {

            		@Override
            		public void response(Map<String, String> __ret) {
            			// TODO Auto-generated method stub
            			CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"现金订单post服务器 createOrderCash: "+__ret);
                        
                		Message msg = new Message();
    					msg.what = BaseHttpHandler.ICE_ROLL_ETOUCH_CASH_SUCCESS;
						msg.obj = __ret;
    					_machineControl.mHttpHandler.sendMessage(msg);
            		}
            		
                    public void exception(Ice.LocalException ex)
                    {
                        destroySession();
                    }

                    public void exception(Ice.UserException ex)
                    {
                    	CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"现金订单post服务器 exception : "+ex.toString());
                    }

                });
            }
            catch(Ice.CommunicatorDestroyedException ex)
            {
                //Ignore client session was destroyed.
            }
        }
    }
    public void createOrderCofficeShop(Map<String, String>[] options)
    {
        if(_machine != null)
        {
            try
            {
                _machine.begin_createOrderCofficeShop(options, new CofficeServer.Callback_MachineSession_createOrderCofficeShop() {

            		@Override
            		public void response(Map<String, String> __ret) {
            			// TODO Auto-generated method stub
            			CommonUtils.showLog(CommonUtils.ICE_TAG,"createOrderCofficeShop: "+__ret);
                        
                		Message msg = new Message();
    					msg.what = BaseHttpHandler.ICE_CREAT_ORDER_COFFICESHOP_SUCCESS;
						msg.obj = __ret;
    					_machineControl.mHttpHandler.sendMessage(msg);
            		}
            		
                    public void exception(Ice.LocalException ex)
                    {
                        destroySession();
                    }

                    public void exception(Ice.UserException ex)
                    {
                    	CommonUtils.showLog(CommonUtils.ICE_TAG,"createOrderCash exception: "+ex.toString());
                    }

                });
            }
            catch(Ice.CommunicatorDestroyedException ex)
            {
                //Ignore client session was destroyed.
            }
        }
    }
    public void createOrderAlipay(Map<String, String> options)
    {
        if(_machine != null)
        {
            try
            {
                _machine.begin_createOrderAlipay(options, new CofficeServer.Callback_MachineSession_createOrderAlipay() {

            		@Override
            		public void response(Map<String, String> __ret) {
            			// TODO Auto-generated method stub
            			CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"alipay订单post服务器 createOrderAlipay: "+__ret);
                		Message msg = new Message();
    					msg.what = BaseHttpHandler.ICE_ROLL_ALI_SONICE_SUCCESS;
						msg.obj = __ret;
    					_machineControl.mHttpHandler.sendMessage(msg);
            		}
            		
                    public void exception(Ice.LocalException ex)
                    {
                        destroySession();
                    }

                    public void exception(Ice.UserException ex)
                    {
                    	CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"alipay订单post服务器 exception: " + ex.toString());
                    }

                });
            }
            catch(Ice.CommunicatorDestroyedException ex)
            {
                //Ignore client session was destroyed.
            }
        }
    }
    public void checkAlipayOrderState(Map<String, String> options)
    {
        if(_machine != null)
        {
            try
            {
                _machine.begin_checkAlipayOrderState(options, new CofficeServer.Callback_MachineSession_checkAlipayOrderState() {

            		@Override
            		public void response(Map<String, String> __ret) {
            			// TODO Auto-generated method stub
            			CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"alipay订单check服务器 checkAlipayOrderState: "+__ret);
                		Message msg = new Message();
    					msg.what = BaseHttpHandler.ICE_ROLL_ALI_SUCCESS;
						msg.obj = __ret;
    					_machineControl.mHttpHandler.sendMessage(msg);
            		}
            		
                    public void exception(Ice.LocalException ex)
                    {
                        destroySession();
                    }

                    public void exception(Ice.UserException ex)
                    {
                    	CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"alipay订单check服务器 exception: "+ex.toString());
                    }

                });
            }
            catch(Ice.CommunicatorDestroyedException ex)
            {
                //Ignore client session was destroyed.
            }
        }
    }
    public void orderComplete(Map<String, String>[] options)
    {
        if(_machine != null)
        {
            try
            {
                _machine.begin_orderComplete(options, new CofficeServer.Callback_MachineSession_orderComplete() {

            		@Override
            		public void response(Map<String, String> __ret) {
            			// TODO Auto-generated method stub
            			CommonUtils.showLog(CommonUtils.ICE_TAG,"orderComplete: "+__ret);
                        
                		Message msg = new Message();
    					msg.what = BaseHttpHandler.ICE_NET_ORDER_SUCCESS;
						msg.obj = __ret;
    					_machineControl.mHttpHandler.sendMessage(msg);
            		}
            		
                    public void exception(Ice.LocalException ex)
                    {
                        destroySession();
                    }

                    public void exception(Ice.UserException ex)
                    {
                    	CommonUtils.showLog(CommonUtils.ICE_TAG,"orderComplete exception: "+ex.toString());
                    }

                });
            }
            catch(Ice.CommunicatorDestroyedException ex)
            {
                //Ignore client session was destroyed.
            }
        }
    }
    public void ping() {
    	try{
        	_machine.ice_ping();
    	} catch (Ice.LocalException e) {
    		destroySession();
    	} catch(Exception ex){
    		
    	} 
    }

    protected void
    destroySession()
    {
        final Glacier2.SessionHelper s = _session;
        _session = null;
        _machine = null;

        _state = ClientState.Disconnected;
        
        if(s != null)
        {
            s.destroy();
        }
    }

    private final Glacier2.SessionFactoryHelper _factory;
    private ClientState _state = ClientState.Disconnected;
    private final String[] _args;
    private Glacier2.SessionHelper _session = null;
    private Object _sessionMonitor = new Object();
    private CofficeServer.MachineSessionPrx _machine = null;
    private String _username = "";
    private String _host = "";
    private boolean _exit = false;
    private final BaseMachineControl _machineControl;
}
