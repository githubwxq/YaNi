package com.handsome.didi.Fragment.Main;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handsome.didi.Activity.Common.WebActivity;
import com.handsome.didi.Activity.Home.SearchActivity;
import com.handsome.didi.Adapter.Category.CategoryLeftAdapter;
import com.handsome.didi.Adapter.Category.CategoryRightAdapter;
import com.handsome.didi.Base.BaseFragment;
import com.handsome.didi.Bean.Category;
import com.handsome.didi.Controller.CategoryController;
import com.handsome.didi.Controller.ShopController;
import com.handsome.didi.Controller.StoreController;
import com.handsome.didi.Controller.UserController;
import com.handsome.didi.R;
import com.handsome.didi.Utils.SpeechUtils;
import com.handsome.didi.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by handsome on 2016/4/7.
 */
public class CategoryFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private Intent intent;
    private CategoryController categoryController;
    //右边栏
    private GridView gv_category;
    private List<Category> category_right_list, category_right_list_show;
    private CategoryRightAdapter right_adapter;
    //左边栏
    private ListView lv_category;
    private List<String> category_left_list;
    private String[] category_left_name = {"潮流男装", "数码家电", "潮流女装", "文娱运动", "潮流鞋品", "儿童用品", "美妆配饰", "母婴频道"};
    private CategoryLeftAdapter left_adapter;
    //头部
    private ImageView iv_speech, iv_zxing;
    private TextView tv_find;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_category;
    }

    @Override
    public void initViews() {
        lv_category = findView(R.id.lv_category);
        gv_category = findView(R.id.gv_category);
        iv_zxing = findView(R.id.iv_zxing);
        iv_speech = findView(R.id.iv_speech);
        tv_find = findView(R.id.tv_find);
    }

    @Override
    public void initData() {
        categoryController = CategoryController.getInstance();
        //初始化左边栏
        initCategoryLeft();
        //初始化右边栏
        initCategoryRight();
    }


    @Override
    public void initListener() {
        setOnClick(iv_zxing);
        setOnClick(iv_speech);
        setOnClick(tv_find);
        lv_category.setOnItemClickListener(this);
        gv_category.setOnItemClickListener(this);
    }

    @Override
    public void processClick(View v) {
        switch (v.getId()) {
            case R.id.iv_zxing:
                //开启扫描界面
                startActivity(CaptureActivity.class);
                break;
            case R.id.iv_speech:
                //语音识别结果
                SpeechUtils.initSpeech(getActivity());
                break;
            case R.id.tv_find:
                //开启查询界面
                startActivity(SearchActivity.class);
                break;
        }
    }

    /**
     * 初始化左边栏
     */
    private void initCategoryLeft() {
        //初始化数据
        category_left_list = new ArrayList<>();
        for (int i = 0; i < category_left_name.length; i++) {
            category_left_list.add(category_left_name[i]);
        }
        //显示数据
        left_adapter = new CategoryLeftAdapter(getActivity(), category_left_list);
        lv_category.setAdapter(left_adapter);
    }

    /**
     * 初始化右边栏
     */
    private void initCategoryRight() {
        //初始化数据
        category_right_list = new ArrayList<>();
        category_right_list_show = new ArrayList<>();
        categoryController.query(new CategoryController.OnBmobListener() {
            @Override
            public void onSuccess(List<?> list) {
                category_right_list = (List<Category>) list;
                initCategoryRightShow();
            }

            @Override
            public void onError(String error) {
                showToast(error);
            }
        });
    }

    /**
     * 右边栏更新有效数据
     */
    private void initCategoryRightShow() {
        //清除数据
        category_right_list_show.clear();
        //取出所要显示的数据
        for (Category category : category_right_list) {
            if (category.sort == left_adapter.getClickPosition()) {
                category_right_list_show.add(category);
            }
        }
        //显示数据
        right_adapter = new CategoryRightAdapter(getActivity(), category_right_list_show);
        gv_category.setAdapter(right_adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getAdapter().equals(left_adapter)) {
            //左边栏点击事件
            left_adapter.setClickPosition(position);
            left_adapter.notifyDataSetChanged();
            //右边栏更新
            initCategoryRightShow();
        } else if (parent.getAdapter().equals(right_adapter)) {
            //右边栏点击事件
            intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra("url", category_right_list_show.get(position).go_url);
            startActivity(intent);
        }
    }
}
