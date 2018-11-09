package com.leiyuntop.Refrigerator;

/**
 * 操作方式
 * 
 * @author MENGQINGYU
 * 
 */
class EDirection
{
	public static final int 写RAM = 0x06;
	public static final int 读RAM = 0x09;
	public static final int 写扩展RAM = 0x16;
	public static final int 读扩展RAM = 0x19;
	public static final int 写ROM = 0x26;
	public static final int 读ROM = 0x29;
}