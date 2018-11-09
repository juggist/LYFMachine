package com.icoffice.library.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import android.annotation.SuppressLint;
import com.icoffice.library.moudle.control.BaseMachineControl;

@SuppressLint("DefaultLocale")
public class GoodsWayUtil {
	public static String SEPARATOR 			= "-";
	
	public static String FOODS_BOX_ID 		= "09";
	public static String DRINKS_BOX_ID 		= "11";
	public static String DRINKS_2_BOX_ID 	= "12";
	public static String GRID_1_BOX_ID 		= "01";
	public static String GRID_2_BOX_ID 		= "02";
	public static String LEIYUN_BOX_ID   	= "21";
	

	public static String FOODS_BOX_NAME 	= "食品柜";
	public static String DRINKS_BOX_NAME 	= "饮料柜";
	public static String DRINKS_2_BOX_NAME 	= "小饮料柜";
	public static String GRID_1_BOX_NAME 	= "格子柜1";
	public static String GRID_2_BOX_NAME 	= "格子柜2";
	public static String LEIYUN_BOX_NAME 	= "雷云峰柜";
	public static int[][] foodBoxArray = {			//货柜类型
													//每行货道对应的id，不可重复
			{1,2,3,4,5,6},							//食品柜_1
	};
	public static int[][] drinkBoxArray = {			//货柜类型
													//每行货道对应的id，不可重复
			{7,8,9,10,11},							//饮料柜_1
	};
	public static int[][] grid1BoxArray = {			//货柜类型
													//每行货道对应的id，不可重复
		{12,13,14,15,16},							//格子柜1_1
	};
	public static int[][] grid2BoxArray = {			//货柜类型
													//每行货道对应的id，不可重复
		{17,18,19,20,21},							//格子柜2_1
	};
	public static int[][] drink2BoxArray = {		//货柜类型
													//每行货道对应的id，不可重复
		{ 22, 23, 24, 25 }, 						//饮料柜_2
	};
	
	public static int[][] leiyunBoxArray = {      	//货柜类型
													//每行货道对应的id，不可重复
													//雷云峰机型
		
	};
	public static int[][] foodArray = {				//食品柜
		{1,2,3,4,5,6,7,8},							//-> 1
		{9,10,11,12,13,14,15,16},					//-> 2
		{17,18,19,20,21,22,23,24},					//-> 3
		{25,26,27,28,29,30,31,32},					//-> 4
		{33,34,35,36,37,38,39,40},					//-> 5
		{41,42,43,44,45,46,47,48}					//-> 6
	};
	public static int[][] drinkArray = {			//饮料柜
		
		{17,15,10,8,3},								//－> 7
		{18,16,11,9,4},								//－> 8
		{19,12,5},									//－> 9
		{20,13,6},									//－> 10
		{21,14,7},									//－> 11						
	};
	public static int[][] grid1Array = {			//格子柜1
		
		{1,2,3,4,5,6,7,8},							//－> 12	
		{9,10,11,12,13,14,15,16},					//－> 13	
		{17,18,19,20,21,22,23,24},					//－> 14
		{25,26,27,28,29,30,31,32},					//－> 15	
		{33,34,35,36,37,38,39,40},					//－> 16	
	};
	public static int[][] grid2Array = {			//格子柜2

		{1,2,3,4,5,6,7,8},							//－> 17	
		{9,10,11,12},								//－> 18
		{13,14,15,16},								//－> 19
		{17,18,19,20},								//－> 20
		{21,22,23,24},								//－> 21			
	};
	public static int[][] drink2Array = {			//饮料柜2
		
		{10,8,3,1},									//－> 7
		{11,9,4,2},									//－> 8
		{12,5},										//－> 9
		{13,6},										//－> 10
	};
	public static String[][] leiyunArray = {		//雷云峰货柜
	};
	/**
	 * @param box
	 * @param road
	 * @return 合并的货道
	 */
	public static String genWayId(String box, String road) {
		return box + SEPARATOR + road;
	}
	/**
	 * 把小饮料机转成饮料机
	 * 12-01 -> 11-01
	 * 
	 * @param w_code
	 * @return
	 */
	public static String getW_code(String w_code){
		String mW_code = w_code;
		int index = w_code.indexOf(SEPARATOR);
		if(index >= 0 && w_code.substring(0, index).equals(DRINKS_2_BOX_ID)){
			mW_code =  DRINKS_BOX_ID + w_code.substring(index);
		}else if(index >= 0 && w_code.substring(0, index).equals(LEIYUN_BOX_ID)){
			mW_code = w_code.substring(index + 1);
		}
		return mW_code;
	}
	/**
	 * @param wayId
	 * @param box
	 * @param road
	 */
	public static ArrayList<String> parseWayId(String wayId) {
		ArrayList<String> list = new ArrayList<String>();
		int separator = wayId.indexOf(SEPARATOR);
		list.add(wayId.substring(0, separator));
		list.add(wayId.substring(separator+1));
		return list;
	}
	/**
	 * 获取机箱类型
	 * @param wayId
	 * @return
	 */
	public static String parseBox(String wayId){
		String boxId = parseWayId(wayId).get(0);
		String boxName = "";
		if(boxId.equals(FOODS_BOX_ID))
			boxName = FOODS_BOX_NAME;
		else if(boxId.equals(DRINKS_BOX_ID))
			boxName = DRINKS_BOX_NAME;
		else if(boxId.equals(DRINKS_2_BOX_ID))
			boxName = DRINKS_2_BOX_NAME;
		else if(boxId.equals(GRID_1_BOX_ID))
			boxName = GRID_1_BOX_NAME;
		else if(boxId.equals(GRID_2_BOX_ID))
			boxName = GRID_2_BOX_NAME;
		else if(boxId.equals(LEIYUN_BOX_ID))
			boxName = LEIYUN_BOX_NAME;
		return boxName;
	}
	/**
	 * 名称转换
	 * 09_01－>食品柜_01;
	 * 11_01－>饮料柜_01;
	 * 01_01－>格子柜一_01;
	 * 02_01－>格子柜二_01;
	 * @param wayId
	 * @return
	 */
	public static String getWayName(String wayId) {
		String name = "";
		try{
			ArrayList<String> list = parseWayId(wayId);
			String box = list.get(0);
			String road = list.get(1);
			
			if (box.equals(FOODS_BOX_ID)) {
				int theRoad = Integer.parseInt(road);
				int row = (theRoad-1)/8 + 1;
				int column = (theRoad-1)%8 + 1;
				name = FOODS_BOX_NAME + SEPARATOR + row + column;
			} else if (box.equals(DRINKS_BOX_ID)) {
				name = DRINKS_BOX_NAME + SEPARATOR + road;
			} else if (box.equals(DRINKS_2_BOX_ID)) {
				name = DRINKS_2_BOX_NAME + SEPARATOR + road;
			}else if (box.equals(GRID_1_BOX_ID)) {
				name = GRID_1_BOX_NAME + SEPARATOR + road;
			} else if (box.equals(GRID_2_BOX_ID)) {
				name = GRID_2_BOX_NAME + SEPARATOR + road;
			} else if (box.equals(LEIYUN_BOX_ID)) {
				name = LEIYUN_BOX_NAME + SEPARATOR + road;
			}
		}catch(Exception e ){
			
		}
		return name;
	}
	/**
	 * 名称转换
	 * 食品柜_01－> 09_01;
	 * 饮料柜_01－> 11_01;
	 * @param wayName
	 * @return
	 */
	public static String getWayId(String wayName){
		String name = "";
		try{
			ArrayList<String> list = parseWayId(wayName);
			String box = list.get(0);
			String road = list.get(1);
			if (box.equals(FOODS_BOX_NAME)) {
				road = String.format("%02d",  (Integer.parseInt(road) - 10) - (Integer.parseInt(road) / 10 - 1) * 2);
				name = FOODS_BOX_ID + SEPARATOR + road;
			} else if (box.equals(DRINKS_BOX_NAME)) {
				name = DRINKS_BOX_ID + SEPARATOR + road;
			} else if (box.equals(DRINKS_2_BOX_NAME)){
				name = DRINKS_2_BOX_ID + SEPARATOR + road;
			}
			else if (box.equals(GRID_1_BOX_NAME)) {
				name = GRID_1_BOX_ID + SEPARATOR + road;
			} else if (box.equals(GRID_2_BOX_NAME)) {
				name = GRID_2_BOX_ID + SEPARATOR + road;
			} else if (box.equals(LEIYUN_BOX_NAME)) {
				name = LEIYUN_BOX_ID + SEPARATOR + road;
			}
		}catch(Exception e ){
			
		}
		return name;
		
	}
	/**
	 * 获取本地开放的货柜信息
	 * @param baseMachineControl
	 * @return
	 */
	public static HashMap<Integer,ArrayList<String>> getWCode(BaseMachineControl baseMachineControl){
		HashMap<Integer,ArrayList<String>> map = new LinkedHashMap<Integer, ArrayList<String>>();
		
		HashMap<ArrayList<String>, HashMap<Integer, ArrayList<String>>> food_map = insertFoodWCode();
		ArrayList<String> food_list = new ArrayList<String>();
		Iterator<Entry<ArrayList<String>, HashMap<Integer, ArrayList<String>>>> food_obj = food_map.entrySet().iterator();
		while(food_obj.hasNext()){
			Entry<ArrayList<String>, HashMap<Integer, ArrayList<String>>> food_item = food_obj.next();
			food_list = food_item.getKey();
			ArrayList<Integer> drink_exist_list = baseMachineControl.selectBox(food_list);
			if(drink_exist_list.get(0) > 0)
				map.putAll(food_item.getValue());
		}
		
		HashMap<ArrayList<String>, HashMap<Integer, ArrayList<String>>> drink_map = insertDrinkWCode();
		ArrayList<String> drink_list = new ArrayList<String>();
		Iterator<Entry<ArrayList<String>, HashMap<Integer, ArrayList<String>>>> drink_obj = drink_map.entrySet().iterator();
		while(drink_obj.hasNext()){
			Entry<ArrayList<String>, HashMap<Integer, ArrayList<String>>> drink_item = drink_obj.next();
			drink_list = drink_item.getKey();
			ArrayList<Integer> drink_exist_list = baseMachineControl.selectBox(drink_list);
			if(drink_exist_list.get(0) > 0)
				map.putAll(drink_item.getValue());
		}
		
		HashMap<ArrayList<String>, HashMap<Integer, ArrayList<String>>> drink_2_map = insertDrink2WCode();
		ArrayList<String> drink_2_list = new ArrayList<String>();
		Iterator<Entry<ArrayList<String>, HashMap<Integer, ArrayList<String>>>> drink_2_obj = drink_2_map.entrySet().iterator();
		while(drink_2_obj.hasNext()){
			Entry<ArrayList<String>, HashMap<Integer, ArrayList<String>>> drink_2_item = drink_2_obj.next();
			drink_2_list = drink_2_item.getKey();
			ArrayList<Integer> drink_2_exist_list = baseMachineControl.selectBox(drink_2_list);
			if(drink_2_exist_list.get(0) > 0)
				map.putAll(drink_2_item.getValue());
		}

		HashMap<ArrayList<String>, HashMap<Integer, ArrayList<String>>> grid_map = insertGridWCode();
		ArrayList<String> grid_list = new ArrayList<String>();
		Iterator<Entry<ArrayList<String>, HashMap<Integer, ArrayList<String>>>> grid_obj = grid_map.entrySet().iterator();
		while(grid_obj.hasNext()){
			Entry<ArrayList<String>, HashMap<Integer, ArrayList<String>>> grid_item = grid_obj.next();
			grid_list = grid_item.getKey();
			ArrayList<Integer> drink_exist_list = baseMachineControl.selectBox(grid_list);
			if(drink_exist_list.get(0) > 0)
				map.putAll(grid_item.getValue());
		}
		
		HashMap<ArrayList<String>, HashMap<Integer, ArrayList<String>>> leiyun_map = insertLYWCode();
		ArrayList<String> leyun_list = new ArrayList<String>();
		Iterator<Entry<ArrayList<String>, HashMap<Integer, ArrayList<String>>>> leiyun_obj = leiyun_map.entrySet().iterator();
		while(leiyun_obj.hasNext()){
			Entry<ArrayList<String>, HashMap<Integer, ArrayList<String>>> leiyun_item = leiyun_obj.next();
			leyun_list = leiyun_item.getKey();
			ArrayList<Integer> leiyun_exist_list = baseMachineControl.selectBox(leyun_list);
			if(leiyun_exist_list.get(0) > 0)
				map.putAll(leiyun_item.getValue());
		}
		return map;
	}
	/**
	 * 获取食品机对应的货道
	 * @return
	 */
	public static ArrayList<String> getFoodWCode(){
		HashMap<ArrayList<String>, HashMap<Integer, ArrayList<String>>> food_map = insertFoodWCode();
		ArrayList<String> list = new ArrayList<String>();
		Iterator<Entry<ArrayList<String>, HashMap<Integer, ArrayList<String>>>> food_obj = food_map.entrySet().iterator();
		while(food_obj.hasNext()){
			Entry<ArrayList<String>, HashMap<Integer, ArrayList<String>>> food_item = food_obj.next();
			list.addAll(food_item.getKey());
		}
		return list;
	}
	/**
	 * 获取饮料机对应的货道
	 * @return
	 */
	public static ArrayList<String> getDrinkWCode(){
		HashMap<ArrayList<String>, HashMap<Integer, ArrayList<String>>> drink_map = insertDrinkWCode();
		ArrayList<String> list = new ArrayList<String>();
		Iterator<Entry<ArrayList<String>, HashMap<Integer, ArrayList<String>>>> drink_obj = drink_map.entrySet().iterator();
		while(drink_obj.hasNext()){
			Entry<ArrayList<String>, HashMap<Integer, ArrayList<String>>> drink_item = drink_obj.next();
			list.addAll(drink_item.getKey());
		}
		return list;
	}
	/**
	 * 获取饮料机2对应的货道
	 * @return
	 */
	public static ArrayList<String> getDrink2WCode(){
		HashMap<ArrayList<String>, HashMap<Integer, ArrayList<String>>> drink_map = insertDrink2WCode();
		ArrayList<String> list = new ArrayList<String>();
		Iterator<Entry<ArrayList<String>, HashMap<Integer, ArrayList<String>>>> drink_obj = drink_map.entrySet().iterator();
		while(drink_obj.hasNext()){
			Entry<ArrayList<String>, HashMap<Integer, ArrayList<String>>> drink_item = drink_obj.next();
			list.addAll(drink_item.getKey());
		}
		return list;
	}
	/**
	 * 获取格子柜对应的货道
	 * @return
	 */
	public static ArrayList<String> getGridWCode(){
		HashMap<ArrayList<String>, HashMap<Integer, ArrayList<String>>> grid_map = insertGridWCode();
		ArrayList<String> list = new ArrayList<String>();
		Iterator<Entry<ArrayList<String>, HashMap<Integer, ArrayList<String>>>> grid_obj = grid_map.entrySet().iterator();
		while(grid_obj.hasNext()){
			Entry<ArrayList<String>, HashMap<Integer, ArrayList<String>>> grid_item = grid_obj.next();
			list.addAll(grid_item.getKey());
		}
		return list;
	}
	/**
	 * 获取雷云峰柜对应的货道
	 * @return
	 */
	public static ArrayList<String> getLeiyunWCode(){
		HashMap<ArrayList<String>, HashMap<Integer, ArrayList<String>>> grid_map = insertLYWCode();
		ArrayList<String> list = new ArrayList<String>();
		Iterator<Entry<ArrayList<String>, HashMap<Integer, ArrayList<String>>>> grid_obj = grid_map.entrySet().iterator();
		while(grid_obj.hasNext()){
			Entry<ArrayList<String>, HashMap<Integer, ArrayList<String>>> grid_item = grid_obj.next();
			list.addAll(grid_item.getKey());
		}
		return list;
	}
	/**
	 * 获取食品机货柜所有信息
	 * @return
	 */
	public static  HashMap<ArrayList<String>, HashMap<Integer,ArrayList<String>>> insertFoodWCode(){
		HashMap<ArrayList<String>, HashMap<Integer,ArrayList<String>>> map = new HashMap<ArrayList<String>, HashMap<Integer,ArrayList<String>>>();
		HashMap<Integer,ArrayList<String>> way_idMap = new LinkedHashMap<Integer, ArrayList<String>>();
		ArrayList<String> food_list = new ArrayList<String>();
		String box = GoodsWayUtil.FOODS_BOX_ID;
		int[] foodBox_itemArray = foodBoxArray[0];
		for(int i = 0;i < foodArray.length;i++){
			int[] food_itemArray = foodArray[i];
			ArrayList<String> way_idList = new ArrayList<String>();
			for(int j = 0;j < food_itemArray.length;j++){
				way_idList.add(GoodsWayUtil.genWayId(box, String.format("%02d", food_itemArray[j])));
			}
			food_list.addAll(way_idList);
			way_idMap.put(foodBox_itemArray[i], way_idList);
 		}
		map.put(food_list, way_idMap);
		return map;
	}
	/**
	 * 获取饮料机货柜所有信息
	 * @return
	 */
	public static  HashMap<ArrayList<String>, HashMap<Integer,ArrayList<String>>> insertDrinkWCode(){
		// 创建饮料柜货道
		HashMap<ArrayList<String>, HashMap<Integer,ArrayList<String>>> map = new HashMap<ArrayList<String>, HashMap<Integer,ArrayList<String>>>();
		HashMap<Integer,ArrayList<String>> way_idMap = new LinkedHashMap<Integer, ArrayList<String>>();
		ArrayList<String> drink_list = new ArrayList<String>();
		String box = GoodsWayUtil.DRINKS_BOX_ID;
		int[] drinkBox_itemArray = drinkBoxArray[0];
		for(int i = 0;i < drinkArray.length;i++){
			int[] drink_itemArray = drinkArray[i];
			ArrayList<String> way_idList = new ArrayList<String>();
			for(int j = 0;j < drink_itemArray.length;j++){
				way_idList.add(GoodsWayUtil.genWayId(box, String.format("%02d", drink_itemArray[j])));
			}
			drink_list.addAll(way_idList);
			way_idMap.put(drinkBox_itemArray[i], way_idList);
		}
		map.put(drink_list, way_idMap);
		return map;
	}
	/**
	 * 获取饮料机货柜2所有信息
	 * @return
	 */
	public static  HashMap<ArrayList<String>, HashMap<Integer,ArrayList<String>>> insertDrink2WCode(){
		// 创建饮料柜货道
		HashMap<ArrayList<String>, HashMap<Integer,ArrayList<String>>> map = new HashMap<ArrayList<String>, HashMap<Integer,ArrayList<String>>>();
		HashMap<Integer,ArrayList<String>> way_idMap = new LinkedHashMap<Integer, ArrayList<String>>();
		ArrayList<String> drink_list = new ArrayList<String>();
		String box = GoodsWayUtil.DRINKS_2_BOX_ID;
		int[] drinkBox_itemArray = drink2BoxArray[0];
		for(int i = 0;i < drink2Array.length;i++){
			int[] drink_itemArray = drink2Array[i];
			ArrayList<String> way_idList = new ArrayList<String>();
			for(int j = 0;j < drink_itemArray.length;j++){
				way_idList.add(GoodsWayUtil.genWayId(box, String.format("%02d", drink_itemArray[j])));
			}
			drink_list.addAll(way_idList);
			way_idMap.put(drinkBox_itemArray[i], way_idList);
		}
		map.put(drink_list, way_idMap);
		return map;
	}
	/**
	 * 获取格子货柜所有信息
	 * @return
	 */
	public static  HashMap<ArrayList<String>, HashMap<Integer,ArrayList<String>>> insertGridWCode(){
		// 创建格子柜货道
		HashMap<ArrayList<String>, HashMap<Integer,ArrayList<String>>> map = new HashMap<ArrayList<String>, HashMap<Integer,ArrayList<String>>>();
		HashMap<Integer,ArrayList<String>> way_idMap = new LinkedHashMap<Integer, ArrayList<String>>();
		//插入格子柜1
		ArrayList<String> grid1_list = new ArrayList<String>();
		String box = GoodsWayUtil.GRID_1_BOX_ID;
		int[] grid1Box_itemArray = grid1BoxArray[0];
		for(int i = 0;i < grid1Array.length;i++){
			int[] grid1_itemArray = grid1Array[i];
			ArrayList<String> way_idList = new ArrayList<String>();
			for(int j = 0;j < grid1_itemArray.length;j++){
				way_idList.add(GoodsWayUtil.genWayId(box, String.format("%02d", grid1_itemArray[j])));
			}
			grid1_list.addAll(way_idList);
			way_idMap.put(grid1Box_itemArray[i], way_idList);
		}
		map.put(grid1_list, way_idMap);
		//插入格子柜2
		ArrayList<String> grid2_list = new ArrayList<String>();
		box = GoodsWayUtil.GRID_2_BOX_ID;
		int[] grid2Box_itemArray = grid2BoxArray[0];
		for(int i = 0;i < grid2Array.length;i++){
			int[] grid2_itemArray = grid2Array[i];
			ArrayList<String> way_idList = new ArrayList<String>();
			for(int j = 0;j < grid2_itemArray.length;j++){
				way_idList.add(GoodsWayUtil.genWayId(box, String.format("%02d", grid2_itemArray[j])));
			}
			grid2_list.addAll(way_idList);
			way_idMap.put(grid2Box_itemArray[i], way_idList);
		}
		map.put(grid2_list, way_idMap);
		return map;
	}
	/**
	 * 获取雷云峰货柜所有信息
	 * @return
	 */
	public static  HashMap<ArrayList<String>, HashMap<Integer,ArrayList<String>>> insertLYWCode(){
		// 创建饮料柜货道
		HashMap<ArrayList<String>, HashMap<Integer,ArrayList<String>>> map = new HashMap<ArrayList<String>, HashMap<Integer,ArrayList<String>>>();
		HashMap<Integer,ArrayList<String>> way_idMap = new LinkedHashMap<Integer, ArrayList<String>>();
		ArrayList<String> leiyun_list = new ArrayList<String>();
		String box = GoodsWayUtil.LEIYUN_BOX_ID;
		int[] leiyunBox_itemArray = leiyunBoxArray[0];
		for(int i = 0;i < leiyunArray.length;i++){
			String[] leiyun_itemArray = leiyunArray[i];
			ArrayList<String> way_idList = new ArrayList<String>();
			for(int j = 0;j < leiyun_itemArray.length;j++){
				way_idList.add(GoodsWayUtil.genWayId(box,leiyun_itemArray[j]));
			}
			leiyun_list.addAll(way_idList);
			way_idMap.put(leiyunBox_itemArray[i], way_idList);
		}
		map.put(leiyun_list, way_idMap);
		return map;
	}
}
