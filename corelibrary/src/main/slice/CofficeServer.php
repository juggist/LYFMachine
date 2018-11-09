<?php
// **********************************************************************
//
// Copyright (c) 2003-2013 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************
//
// Ice version 3.5.1
//
// <auto-generated>
//
// Generated from file `CofficeServer.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

require_once 'Ice/BuiltinSequences.php';
require_once 'Glacier2/Session.php';

if(!defined('CofficeServer_MsgNewApkVersion'))
{
    define('CofficeServer_MsgNewApkVersion', "MSG_NEW_APK_VERSION");
}

if(!defined('CofficeServer_MsgNewAds'))
{
    define('CofficeServer_MsgNewAds', "MSG_NEW_ADS");
}

if(!defined('CofficeServer_MsgConfigChanged'))
{
    define('CofficeServer_MsgConfigChanged', "MSG_CONFIG_CHANGED");
}

if(!class_exists('CofficeServer_MachineType'))
{
    class CofficeServer_MachineType
    {
        const UnknownMachine = 0;
        const Drink = 1;
        const Machine = 2;
    }

    $CofficeServer__t_MachineType = IcePHP_defineEnum('::CofficeServer::MachineType', array('UnknownMachine', 0, 'Drink', 1, 'Machine', 2));
}

if(!class_exists('CofficeServer_PayType'))
{
    class CofficeServer_PayType
    {
        const UnknownPay = 0;
        const cash = 1;
        const unionpay = 2;
        const wechat = 3;
        const ali = 4;
        const card = 5;
        const exchangepay = 6;
        const yeepay = 7;
    }

    $CofficeServer__t_PayType = IcePHP_defineEnum('::CofficeServer::PayType', array('UnknownPay', 0, 'cash', 1, 'unionpay', 2, 'wechat', 3, 'ali', 4, 'card', 5, 'exchangepay', 6, 'yeepay', 7));
}

if(!isset($CofficeServer__t_Dict))
{
    $CofficeServer__t_Dict = IcePHP_defineDictionary('::CofficeServer::Dict', $IcePHP__t_string, $IcePHP__t_string);
}

if(!isset($CofficeServer__t_Dicts))
{
    $CofficeServer__t_Dicts = IcePHP_defineSequence('::CofficeServer::Dicts', $CofficeServer__t_Dict);
}

if(!interface_exists('CofficeServer_MachineCallback'))
{
    interface CofficeServer_MachineCallback
    {
        public function queryStatus($options);
        public function pushMessage($message, $options);
    }

    class CofficeServer_MachineCallbackPrxHelper
    {
        public static function checkedCast($proxy, $facetOrCtx=null, $ctx=null)
        {
            return $proxy->ice_checkedCast('::CofficeServer::MachineCallback', $facetOrCtx, $ctx);
        }

        public static function uncheckedCast($proxy, $facet=null)
        {
            return $proxy->ice_uncheckedCast('::CofficeServer::MachineCallback', $facet);
        }
    }

    $CofficeServer__t_MachineCallback = IcePHP_defineClass('::CofficeServer::MachineCallback', 'CofficeServer_MachineCallback', -1, true, false, $Ice__t_Object, null, null);

    $CofficeServer__t_MachineCallbackPrx = IcePHP_defineProxy($CofficeServer__t_MachineCallback);

    IcePHP_defineOperation($CofficeServer__t_MachineCallback, 'queryStatus', 0, 0, 0, array(array($CofficeServer__t_Dict, false, 0)), null, array($CofficeServer__t_Dict, false, 0), null);
    IcePHP_defineOperation($CofficeServer__t_MachineCallback, 'pushMessage', 0, 0, 0, array(array($IcePHP__t_string, false, 0), array($CofficeServer__t_Dict, false, 0)), null, null, null);
}

if(!interface_exists('CofficeServer_MachineSession'))
{
    interface CofficeServer_MachineSession extends Glacier2_Session
    {
        public function setCallback($cb);
        public function queryMachineGoods($options);
        public function heartBeat($options);
        public function createOrderCash($orders);
        public function createOrderUnionPay($orders);
        public function createOrderCofficeShop($orders);
        public function createOrderAlipay($options);
        public function createOrderCofficeCard($options);
        public function checkWeixinOrderState($options);
        public function checkAlipayOrderState($options);
        public function orderComplete($orders);
        public function downloadApk($options);
        public function getConfig($machineCode);
    }

    class CofficeServer_MachineSessionPrxHelper
    {
        public static function checkedCast($proxy, $facetOrCtx=null, $ctx=null)
        {
            return $proxy->ice_checkedCast('::CofficeServer::MachineSession', $facetOrCtx, $ctx);
        }

        public static function uncheckedCast($proxy, $facet=null)
        {
            return $proxy->ice_uncheckedCast('::CofficeServer::MachineSession', $facet);
        }
    }

    $CofficeServer__t_MachineSession = IcePHP_defineClass('::CofficeServer::MachineSession', 'CofficeServer_MachineSession', -1, true, false, $Ice__t_Object, array($Glacier2__t_Session), null);

    $CofficeServer__t_MachineSessionPrx = IcePHP_defineProxy($CofficeServer__t_MachineSession);

    IcePHP_defineOperation($CofficeServer__t_MachineSession, 'setCallback', 0, 0, 0, array(array($CofficeServer__t_MachineCallbackPrx, false, 0)), null, null, null);
    IcePHP_defineOperation($CofficeServer__t_MachineSession, 'queryMachineGoods', 0, 0, 0, array(array($CofficeServer__t_Dict, false, 0)), null, array($CofficeServer__t_Dict, false, 0), null);
    IcePHP_defineOperation($CofficeServer__t_MachineSession, 'heartBeat', 0, 0, 0, array(array($CofficeServer__t_Dict, false, 0)), null, array($CofficeServer__t_Dict, false, 0), null);
    IcePHP_defineOperation($CofficeServer__t_MachineSession, 'createOrderCash', 0, 0, 0, array(array($CofficeServer__t_Dicts, false, 0)), null, array($CofficeServer__t_Dict, false, 0), null);
    IcePHP_defineOperation($CofficeServer__t_MachineSession, 'createOrderUnionPay', 0, 0, 0, array(array($CofficeServer__t_Dicts, false, 0)), null, array($CofficeServer__t_Dict, false, 0), null);
    IcePHP_defineOperation($CofficeServer__t_MachineSession, 'createOrderCofficeShop', 0, 0, 0, array(array($CofficeServer__t_Dicts, false, 0)), null, array($CofficeServer__t_Dict, false, 0), null);
    IcePHP_defineOperation($CofficeServer__t_MachineSession, 'createOrderAlipay', 0, 0, 0, array(array($CofficeServer__t_Dict, false, 0)), null, array($CofficeServer__t_Dict, false, 0), null);
    IcePHP_defineOperation($CofficeServer__t_MachineSession, 'createOrderCofficeCard', 0, 0, 0, array(array($CofficeServer__t_Dict, false, 0)), null, array($CofficeServer__t_Dict, false, 0), null);
    IcePHP_defineOperation($CofficeServer__t_MachineSession, 'checkWeixinOrderState', 0, 0, 0, array(array($CofficeServer__t_Dict, false, 0)), null, array($CofficeServer__t_Dict, false, 0), null);
    IcePHP_defineOperation($CofficeServer__t_MachineSession, 'checkAlipayOrderState', 0, 0, 0, array(array($CofficeServer__t_Dict, false, 0)), null, array($CofficeServer__t_Dict, false, 0), null);
    IcePHP_defineOperation($CofficeServer__t_MachineSession, 'orderComplete', 0, 0, 0, array(array($CofficeServer__t_Dicts, false, 0)), null, array($CofficeServer__t_Dict, false, 0), null);
    IcePHP_defineOperation($CofficeServer__t_MachineSession, 'downloadApk', 0, 0, 0, array(array($CofficeServer__t_Dict, false, 0)), null, array($CofficeServer__t_Dict, false, 0), null);
    IcePHP_defineOperation($CofficeServer__t_MachineSession, 'getConfig', 0, 0, 0, array(array($IcePHP__t_string, false, 0)), null, array($CofficeServer__t_Dict, false, 0), null);
}

if(!interface_exists('CofficeServer_ServerManager'))
{
    interface CofficeServer_ServerManager
    {
        public function queryMachineStatus($id);
        public function pushMachineMessage($id, $message, $options);
    }

    class CofficeServer_ServerManagerPrxHelper
    {
        public static function checkedCast($proxy, $facetOrCtx=null, $ctx=null)
        {
            return $proxy->ice_checkedCast('::CofficeServer::ServerManager', $facetOrCtx, $ctx);
        }

        public static function uncheckedCast($proxy, $facet=null)
        {
            return $proxy->ice_uncheckedCast('::CofficeServer::ServerManager', $facet);
        }
    }

    $CofficeServer__t_ServerManager = IcePHP_defineClass('::CofficeServer::ServerManager', 'CofficeServer_ServerManager', -1, true, false, $Ice__t_Object, null, null);

    $CofficeServer__t_ServerManagerPrx = IcePHP_defineProxy($CofficeServer__t_ServerManager);

    IcePHP_defineOperation($CofficeServer__t_ServerManager, 'queryMachineStatus', 0, 0, 0, array(array($IcePHP__t_string, false, 0)), null, array($CofficeServer__t_Dict, false, 0), null);
    IcePHP_defineOperation($CofficeServer__t_ServerManager, 'pushMachineMessage', 0, 0, 0, array(array($IcePHP__t_string, false, 0), array($IcePHP__t_string, false, 0), array($CofficeServer__t_Dict, false, 0)), null, null, null);
}
?>