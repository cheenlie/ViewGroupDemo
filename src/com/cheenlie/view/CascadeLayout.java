package com.cheenlie.view;
import com.cheenlie.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class CascadeLayout extends ViewGroup {
	private int mHorizontalSpacing;
	private int mVerticalSpacing;
	
	//当通过xml文件创建改视图实例时会调用该构造函数
	public CascadeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.CascadeLayout);
		
//	    <declare-styleable name="CascadeLayout">
//	        <attr name="horizontal_spacing" format="dimension"></attr>
//	    </declare-styleable>
		try {
			mHorizontalSpacing=array.getDimensionPixelSize(R.styleable.CascadeLayout_horizontal_spacing, getResources().getDimensionPixelSize(R.dimen.cascade_horizontal_spacing));
			mVerticalSpacing=array.getDimensionPixelSize(R.styleable.CascadeLayout_horizontal_spacing, getResources().getDimensionPixelSize(R.dimen.cascade_vertical_spacing));
		} finally  {
			array.recycle();
		}
//一个try-finally结构，只要try块开始执行了，finally块里面的代码保证执行一次并且只有一次。
	}
	
	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		//测试它左边的对象是否是它右边的类的实例，返回boolean类型的数据
		 return p instanceof LayoutParams;  
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LayoutParams(getContext(),attrs);
	}

	@Override
	protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
		return new LayoutParams(p.width, p.height);
	}

	public class LayoutParams extends ViewGroup.LayoutParams{

		int x;
		int y;
		
		private int vertialSpacing;
		
		public LayoutParams(Context context, AttributeSet attrs) {
			super(context, attrs);
			TypedArray array2=context.obtainStyledAttributes(attrs, R.styleable.CascadeLayout_LayoutParams);
			
			try {
				vertialSpacing=array2.getDimensionPixelSize(R.styleable.CascadeLayout_LayoutParams_layout_vertical_spacing, -1);
			} finally {
				array2.recycle();
			}
		}
		
		public LayoutParams(int w,int h){
			super(w,h);
		}
	}

	// 重写view的onMeasure方法
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		//使用宽和高计算布局的最终大小 以及视图的x与y轴位置
		int width=getPaddingLeft();
		int height=getPaddingTop();
		 int verticalSpacing;
		
		final int count=getChildCount();
		for(int i=0;i<count;i++){
			verticalSpacing=mVerticalSpacing;
			
			View child=getChildAt(i);
			//令每个孩子测量自己
			measureChild(child,widthMeasureSpec, heightMeasureSpec);
			
			LayoutParams lp=(LayoutParams) child.getLayoutParams();
			width=getPaddingLeft()+mHorizontalSpacing*i;
			
			//在LayoutParams中保存每个子视图的x和y坐标
			lp.x=width;
			lp.y=height;
			
			if(lp.vertialSpacing>=0){
				verticalSpacing=lp.vertialSpacing;
			}
			
			width+=child.getMeasuredWidth();
//			height+=mVerticalSpacing;  //书上问题
			height+=verticalSpacing;
		}
		
		width+=getPaddingRight();
		height+=getChildAt(getChildCount()-1).getMeasuredHeight()+getPaddingBottom();
		
		//使用计算所得的宽和高设置整个布局的测量尺寸
		setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec));
	}
	
// Position all children within this layout.
	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		final int count=getChildCount();
		for(int i=0;i<count;i++){
			View child=getChildAt(i);
			
			LayoutParams lp=(LayoutParams) child.getLayoutParams();
			child.layout(lp.x, lp.y, lp.x+child.getMeasuredWidth(), lp.y+child.getMeasuredHeight());
		}
	}
}
