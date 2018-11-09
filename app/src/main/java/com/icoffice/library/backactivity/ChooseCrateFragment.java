package com.icoffice.library.backactivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.icoffice.library.utils.GoodsWayUtil;
import com.icofficeapp.R;

import java.util.ArrayList;
/**
 * 易触
 * 设置机箱拖柜
 * @author lufeisong
 *
 */
@SuppressLint("ValidFragment")
public class ChooseCrateFragment extends Fragment implements OnClickListener{
	private BaseFragmentActivity mContext;
	private CheckBox rb_drink,rb_drink_2,rb_food,rb_grid,rb_ly;
	private Button btn_commit;
	
	private ArrayList<Integer> drink_exist_list = new ArrayList<Integer>();	//获取货柜信息
																// ArrayList<Integer> list 
																//list.get(0)_存在的货道数量 
																//list.get(1)_货道对应存在的商品g_code
	private ArrayList<Integer> drink_2_exist_list = new ArrayList<Integer>();	//获取货柜信息
																// ArrayList<Integer> list 
																//list.get(0)_存在的货道数量 
																//list.get(1)_货道对应存在的商品g_code
	private ArrayList<Integer> food_exist_list = new ArrayList<Integer>();	//获取货柜信息
																// ArrayList<Integer> list 
																//list.get(0)_存在的货道数量 
																//list.get(1)_货道对应存在的商品g_code
	private ArrayList<Integer> grid_exist_list = new ArrayList<Integer>();	//获取货柜信息
	private ArrayList<Integer> leiyun_exist_list = new ArrayList<Integer>();	//获取货柜信息
	
	protected ArrayList<String> drink_list = new ArrayList<String>();
	protected ArrayList<String> drink_2_list = new ArrayList<String>();
	protected ArrayList<String> food_list = new ArrayList<String>();
	protected ArrayList<String> grid_list = new ArrayList<String>();
	protected ArrayList<String> leiyun_list = new ArrayList<String>();
	public ChooseCrateFragment(BaseFragmentActivity context){
		mContext = context;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.library_choose_crate, null);
		initView(view);
		initListener();
		initData();
		return view;
	}
	void initView(View view){
		rb_drink = (CheckBox) view.findViewById(R.id.library_choose_crate_drink);
		rb_drink_2 = (CheckBox) view.findViewById(R.id.library_choose_crate_drink2);
		rb_food = (CheckBox) view.findViewById(R.id.library_choose_crate_food);
		rb_grid = (CheckBox) view.findViewById(R.id.library_choose_crate_grid);
		rb_ly = (CheckBox) view.findViewById(R.id.library_choose_crate_leiyun);
		btn_commit = (Button) view.findViewById(R.id.library_crate_commit);
		
	}
	void initListener(){
		btn_commit.setOnClickListener(this);
		rb_drink.setOnCheckedChangeListener(listener);
		rb_drink_2.setOnCheckedChangeListener(listener);
		rb_food.setOnCheckedChangeListener(listener);
		rb_grid.setOnCheckedChangeListener(listener);
		rb_ly.setOnCheckedChangeListener(listener);
	}
	void initData(){
		food_list = GoodsWayUtil.getFoodWCode();
		drink_list = GoodsWayUtil.getDrinkWCode();
		drink_2_list = GoodsWayUtil.getDrink2WCode();
		grid_list = GoodsWayUtil.getGridWCode();
		leiyun_list = GoodsWayUtil.getLeiyunWCode();
		
		drink_exist_list = mContext.mBaseMachineControl.selectBox(drink_list);
		drink_2_exist_list = mContext.mBaseMachineControl.selectBox(drink_2_list);
		food_exist_list = mContext.mBaseMachineControl.selectBox(food_list);
		grid_exist_list = mContext.mBaseMachineControl.selectBox(grid_list);
		leiyun_exist_list = mContext.mBaseMachineControl.selectBox(leiyun_list);
		
		if(drink_exist_list.get(0) > 0 )
			rb_drink.setChecked(true);
		if(drink_2_exist_list.get(0) > 0 )
			rb_drink_2.setChecked(true);
		if(food_exist_list.get(0) > 0 )
			rb_food.setChecked(true);
		if(grid_exist_list.get(0) > 0)
			rb_grid.setChecked(true);
		if(leiyun_exist_list.get(0) > 0)
			rb_ly.setChecked(true);
	}

	private OnCheckedChangeListener listener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if(buttonView.getId() == rb_food.getId()){
				if(isChecked){
					
				}else{
					if(food_exist_list.get(1) > 0){
						rb_food.setChecked(true);
						mContext.commentToast.show("该货柜有库存，不能解除该货柜");
					}
				}
			}else if(buttonView.getId() == rb_drink.getId()){
				if(isChecked){
					if(rb_drink_2.isChecked()){
						rb_drink.setChecked(false);
						mContext.commentToast.show("饮料柜存在，不能打开该货柜");
					}
				}else{
					if(drink_exist_list.get(1) > 0){
						rb_drink.setChecked(true);
						mContext.commentToast.show("该货柜有库存，不能解除该货柜");
					}
				}
			}else if(buttonView.getId() == rb_drink_2.getId()){
				if(isChecked){
					if(rb_drink.isChecked()){
						rb_drink_2.setChecked(false);
						mContext.commentToast.show("饮料柜存在，不能打开该货柜");
					}
				}else{
					if(drink_2_exist_list.get(1) > 0){
						rb_drink_2.setChecked(true);
						mContext.commentToast.show("该货柜有库存，不能解除该货柜");
					}
				}
			}else if(buttonView.getId() == rb_grid.getId()){
				if(isChecked){
					
				}else{
					if(grid_exist_list.get(1) > 0){
						rb_grid.setChecked(true);
						mContext.commentToast.show("该货柜有库存，不能解除该货柜");
					}
				}
			}else if(buttonView.getId() == rb_ly.getId()){
				if(isChecked){
					
				}else{
					if(leiyun_exist_list.get(1) > 0){
						rb_ly.setChecked(true);
						mContext.commentToast.show("该货柜有库存，不能解除该货柜");
					}
				}
			}
		}
		
	};
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.library_crate_commit:
			if(drink_exist_list.get(0) == 0 && rb_drink.isChecked()){//加货柜
				mContext.mBaseMachineControl.mDbHelper.insertWayBean(drink_list);
			}else if(drink_exist_list.get(0) > 0 && !rb_drink.isChecked()){//解除货柜
				mContext.mBaseMachineControl.deleteBox(drink_list);
			}
			if(drink_2_exist_list.get(0) == 0 && rb_drink_2.isChecked()){//加货柜
				mContext.mBaseMachineControl.mDbHelper.insertWayBean(drink_2_list);
			}else if(drink_2_exist_list.get(0) > 0 && !rb_drink_2.isChecked()){//解除货柜
				mContext.mBaseMachineControl.deleteBox(drink_2_list);
			}
			if(food_exist_list.get(0) == 0 && rb_food.isChecked()){//加货柜
				mContext.mBaseMachineControl.mDbHelper.insertWayBean(food_list);
			}else if(food_exist_list.get(0) > 0 && !rb_food.isChecked()){//解除货柜
				mContext.mBaseMachineControl.deleteBox(food_list);
			}
			if(grid_exist_list.get(0) == 0 && rb_grid.isChecked()){//加货柜
				mContext.mBaseMachineControl.mDbHelper.insertWayBean(grid_list);
			}else if(grid_exist_list.get(0) > 0 && !rb_grid.isChecked()){//解除货柜
				mContext.mBaseMachineControl.deleteBox(grid_list);
			}
			if(leiyun_exist_list.get(0) == 0 && rb_ly.isChecked()){//加货柜
				mContext.mBaseMachineControl.mDbHelper.insertWayBean(leiyun_list);
			}else if(leiyun_exist_list.get(0) > 0 && !rb_ly.isChecked()){//解除货柜
				mContext.mBaseMachineControl.deleteBox(leiyun_list);
			}
			mContext.commentToast.show("修改货柜信息成功");
			break;
		}
	}
}
