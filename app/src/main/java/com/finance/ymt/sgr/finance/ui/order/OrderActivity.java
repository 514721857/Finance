package com.finance.ymt.sgr.finance.ui.order;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemChildClickListener;
import com.finance.ymt.sgr.finance.R;
import com.finance.ymt.sgr.finance.config.AppCon;
import com.finance.ymt.sgr.finance.config.MvpWebSocketActivity;
import com.finance.ymt.sgr.finance.model.OrderBean;
import com.finance.ymt.sgr.finance.model.oneArea;
import com.finance.ymt.sgr.finance.ui.LoginActivity;
import com.finance.ymt.sgr.finance.ui.adapter.OrderListAdapter;
import com.finance.ymt.sgr.finance.ui.order.OrderPresenter;
import com.finance.ymt.sgr.finance.ui.order.OrderView;
import com.finance.ymt.sgr.finance.util.StartActivityUtil;
import com.finance.ymt.sgr.finance.util.ToastUtils;
import com.finance.ymt.sgr.finance.view.MyDialog;
import com.google.gson.Gson;

import com.zhangke.websocket.ErrorResponse;
import com.zhangke.websocket.Response;




import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class OrderActivity extends MvpWebSocketActivity<OrderView,OrderPresenter> implements OrderView ,OnItemChildClickListener{

    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;


    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private OrderListAdapter mAdapter;

    private int mNextRequestPage = 0;
    private static final int PAGE_SIZE = 10;
    SharedPreferences pref ;
    SharedPreferences.Editor editor;

    String address;
    private int status=0;//默认是待制作
    private int lastWm=0;
    private int lastZq=0;
    private int type=1;//默认是外卖
    private String userID;





    @BindView(R.id.order_text_dfk)
    TextView order_text_dfk;

    @BindView(R.id.order_text_psz)
    TextView order_text_psz;

    @BindView(R.id.order_text_ywc)
    TextView order_text_ywc;

    @BindView(R.id.order_text_yqx)
    TextView order_text_yqx;


    @BindView(R.id.order_text_dzz)
    TextView order_text_dzz;

    @BindView(R.id.order_text_dps)
    TextView order_text_dps;

    @BindView(R.id.order_text_zzz)
    TextView order_text_zzz;



    @BindView(R.id.zq_dzz)
    TextView zq_dzz;

    @BindView(R.id.zq_zqx)
    TextView zq_zqx;

    @BindView(R.id.zq_dqc)
    TextView zq_dqc;

    @BindView(R.id.zq_yqc)
    TextView zq_yqc;

    @BindView(R.id.top_view_left)
    TextView top_view_left;


    @BindView(R.id.order_btn_wm)
    Button order_btn_wm;

    @BindView(R.id.order_btn_zq)
    Button order_btn_zq;

    @BindView(R.id.wm_bottom_view)
    View wm_bottom_view;

    @BindView(R.id.zq_bottom_view)
    View zq_bottom_view;

    @BindView(R.id.zq_dfk)
    TextView zq_dfk;



    Badge zq_badge_but,wm_badeg_but;

    private  boolean isWm,isZq;


    //设置默认
    private void setMoren(){


        isWm=true;
        order_btn_wm.setTextColor(ContextCompat.getColor(this, R.color.colorRed));

        order_btn_wm.setBackgroundResource(R.drawable.touch_bg_select);
        order_btn_zq.setTextColor(ContextCompat.getColor(this, R.color.bottom_text));
        wm_bottom_view.setVisibility(View.VISIBLE);
        zq_bottom_view.setVisibility(View.GONE);
        setWmChoice(0);
        setzQChoice(4);

        zq_badge_but=new QBadgeView(this).bindTarget(order_btn_zq).setBadgeGravity(Gravity.END | Gravity.TOP);
        wm_badeg_but=new QBadgeView(this).bindTarget(order_btn_wm).setBadgeGravity(Gravity.END | Gravity.TOP);

  /*      if(isZq){
            order_btn_zq.setTextColor(ContextCompat.getColor(this, R.color.colorRed));
            order_btn_wm .setTextColor(ContextCompat.getColor(this, R.color.bottom_text));
            isWm=false;
        }else{
            order_btn_wm.setTextColor(ContextCompat.getColor(this, R.color.colorRed));
            order_btn_zq.setTextColor(ContextCompat.getColor(this, R.color.bottom_text));
            isZq=false;
        }*/
    }



    private String headers[] = {"外卖", "自取"};
    private List<View> popupViews = new ArrayList<>();




    //    private String ages[] = {"不限", "18岁以下", "18-22岁", "23-26岁", "27-35岁", "35岁以上"};
    private String sexs[] = { "待配送", "已完成","已取消"};

    private int constellationPosition = 0;
    BluetoothAdapter blueadapter;//蓝牙适配器
    private View dialogView,dialogView2,dialogView3;
    private ArrayAdapter<String> adapter1,adapter2,adapter3;
    private ArrayList<String> deviceList_bonded=new ArrayList<String>();
    private ArrayList<String> deviceList_found=new ArrayList<String>();
    private ListView lv1,lv2,lv_usb;
    private LinearLayout ll1;
    Button btn_scan;
    AlertDialog dialog;



    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }
    @OnClick({R.id.top_view_right_text,R.id.layout_zq_dqc,R.id.layout_zq_zqx,R.id.layout_zq_dzz,R.id.layout_dps,R.id.layout_zzz,R.id.layout_dzz
            ,R.id.top_view_left,R.id.layout_zq_yqc,R.id.order_btn_wm,R.id.order_btn_zq,R.id.layout_wm_dfk,R.id.layout_wm_psz,R.id.layout_zq_dfk,
            R.id.layout_wm_ywc,R.id.layout_wm_yqx})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order_btn_wm://外卖

                zq_badge_but.setBadgeText("");
//                zq_badge_text.hide(true);
//                zq_badge_text.hide(true);
                type=1;
//                this.sendText("测试");
                if(!isWm){

                    order_btn_wm.setTextColor(ContextCompat.getColor(this, R.color.colorRed));
                    order_btn_wm.setBackgroundResource(R.drawable.touch_bg_select);
                    order_btn_zq .setTextColor(ContextCompat.getColor(this, R.color.bottom_text));
                    order_btn_zq.setBackgroundResource(R.drawable.touch_bg);
                    wm_bottom_view.setVisibility(View.VISIBLE);
                    zq_bottom_view.setVisibility(View.GONE);
                    isZq=false;
                    status=lastWm;
                    refresh();

                }
                break;
            case R.id.order_btn_zq://自取
                type=0;
                if(!isZq){

                    order_btn_zq.setTextColor(ContextCompat.getColor(this, R.color.colorRed));
                    order_btn_zq.setBackgroundResource(R.drawable.touch_bg_select);
                    order_btn_wm .setTextColor(ContextCompat.getColor(this, R.color.bottom_text));
                    order_btn_wm.setBackgroundResource(R.drawable.touch_bg);
                    zq_bottom_view.setVisibility(View.VISIBLE);
                    wm_bottom_view .setVisibility(View.GONE);
                    isWm=false;

                    status=lastZq;

                    refresh();

                }
//                setzQChoice(3);
                break;

            case R.id.layout_zq_dfk://待付款 自取
                status=lastZq=0;
                refresh();
                setzQChoice(4);
                break;
            case R.id.layout_zq_yqc://已取餐
                status=lastZq=4;
                refresh();
                setzQChoice(3);
                break;
            case R.id.layout_zq_dqc://待取餐
                status=lastZq=3;
                refresh();
                setzQChoice(2);
                break;
            case R.id.layout_zq_zqx://自取 已取消
                status= lastZq=-1;
                refresh();
                setzQChoice(1);
                break;
            case R.id.layout_zq_dzz:// 自取待制作
                status= lastZq=1;
                refresh();

                setzQChoice(0);
                break;
            /////////////////////////////////////////////外卖
            case R.id.layout_dps:// 待配送
                status=lastWm=3;
                refresh();
                setWmChoice(3);

                break;
            case R.id.layout_zzz:// 制作中
                status=lastWm=2;
                refresh();
                setWmChoice(2);
                break;
            case R.id.layout_dzz:// 待制作
                status= lastWm=1;
                refresh();
                setWmChoice(1);
                break;
            case R.id.layout_wm_yqx:// 待制作
                status= lastWm=-1;
                refresh();

                setWmChoice(6);
                break;
            case R.id.layout_wm_ywc:// 已完成
                status= lastWm=5;
                refresh();

                setWmChoice(5);
                break;
            case R.id.layout_wm_psz:// 配送中
                status= lastWm=4;
                refresh();
                setWmChoice(4);
                break;
            case R.id.layout_wm_dfk:// 待付款
                status= lastWm=0;
                refresh();
                setWmChoice(0);
                break;


            case R.id.top_view_right_text:

                editor = pref.edit();
                editor.putString(AppCon.SCCESS_TOKEN_KEY,"");
                editor.commit();
                StartActivityUtil.skipAnotherActivity(this, LoginActivity.class);
                break;



        }
    }
    private void setzQChoice(int i){
        clearZq();
        if(i==0){
            zq_dzz.setTextColor(ContextCompat.getColor(this, R.color.colorRed));
        }else if(i==1){
            zq_zqx.setTextColor(ContextCompat.getColor(this, R.color.colorRed));
        }else if(i==2){
            zq_dqc.setTextColor(ContextCompat.getColor(this, R.color.colorRed));
        }else if(i==3){
            zq_yqc.setTextColor(ContextCompat.getColor(this, R.color.colorRed));
        }else{
            zq_dfk.setTextColor(ContextCompat.getColor(this, R.color.colorRed));
        }
    }
    private void clearZq(){
        zq_dfk.setTextColor(ContextCompat.getColor(this, R.color.bottom_text));
        zq_yqc.setTextColor(ContextCompat.getColor(this, R.color.bottom_text));
        zq_dqc.setTextColor(ContextCompat.getColor(this, R.color.bottom_text));
        zq_zqx.setTextColor(ContextCompat.getColor(this, R.color.bottom_text));
        zq_dzz.setTextColor(ContextCompat.getColor(this, R.color.bottom_text));
    }

    private void setWmChoice(int i){
        clearWm();
        if(i==0){
            order_text_dfk.setTextColor(ContextCompat.getColor(this, R.color.colorRed));
        }else if(i==1){
            order_text_dzz.setTextColor(ContextCompat.getColor(this, R.color.colorRed));
        }else if(i==2){
            order_text_zzz.setTextColor(ContextCompat.getColor(this, R.color.colorRed));
        }else if(i==3){
            order_text_dps.setTextColor(ContextCompat.getColor(this, R.color.colorRed));
        }else if(i==4){
            order_text_psz.setTextColor(ContextCompat.getColor(this, R.color.colorRed));
        }else if(i==5){
            order_text_ywc.setTextColor(ContextCompat.getColor(this, R.color.colorRed));
        }else if(i==6){
            order_text_yqx.setTextColor(ContextCompat.getColor(this, R.color.colorRed));
        }
    }
    private void clearWm(){
        order_text_dzz.setTextColor(ContextCompat.getColor(this, R.color.bottom_text));
        order_text_dps.setTextColor(ContextCompat.getColor(this, R.color.bottom_text));
        order_text_zzz.setTextColor(ContextCompat.getColor(this, R.color.bottom_text));

        order_text_dfk.setTextColor(ContextCompat.getColor(this, R.color.bottom_text));
        order_text_psz.setTextColor(ContextCompat.getColor(this, R.color.bottom_text));
        order_text_ywc.setTextColor(ContextCompat.getColor(this, R.color.bottom_text));
        order_text_yqx.setTextColor(ContextCompat.getColor(this, R.color.bottom_text));

    }



    @Override
    protected void initView() {
        super.initView();
        //绑定service，获取ImyBinder对象

        pref = this.getSharedPreferences(AppCon.USER_KEY,MODE_PRIVATE);
        userID= pref.getString(AppCon.USER_USER_ID,"");


        setMoren();
        initMenu();
//        getPresenter().getAddress1();
        //init city menu

    }

    private void initMenu(){
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        mAdapter = new OrderListAdapter(this);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(this);
        refresh();
    }


    private void loadMore() {
        getPresenter().getOrderList(status,mNextRequestPage,type);
    }

    private void refresh() {
        mNextRequestPage = 0;
        mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        getPresenter().getOrderList(status,mNextRequestPage,type);
    }


    private void setData(boolean isRefresh, List data) {
        mNextRequestPage++;
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            mAdapter.setNewData(data);
        } else {
            if (size > 0) {
                mAdapter.addData(data);
            }
        }
        if (size < PAGE_SIZE) {
            //第一页如果不够一页就不显示没有更多数据布局
            mAdapter.loadMoreEnd(isRefresh);
//            Toast.makeText(this, "no more data", Toast.LENGTH_SHORT).show();
        } else {
            mAdapter.loadMoreComplete();
        }
    }

    @Override
    public OrderPresenter createPresenter() {
        return new OrderPresenter(this);
    }

    @Override
    public void UpdateSussess(int position) {
        mAdapter.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void ResultAddress1(List<oneArea> reslt) {
//        oneAreas=reslt;
        initMenu();
    }

    @Override
    public void showResult(List<OrderBean> result) {
        if(result!=null){
            System.out.println("显示数据"+result.size());
        }else{
            System.out.println("没有数据");
        }

        if(mNextRequestPage==0){
            setData(true,result);
            mAdapter.setEnableLoadMore(true);
            mSwipeRefreshLayout.setRefreshing(false);
        }else{
            setData(false, result);
        }


    }

    @Override
    public void showResultOnErr(String err) {
        Toast.makeText(this,err,Toast.LENGTH_SHORT).show();
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onItemChildClick(final BaseQuickAdapter adapter, View view,final int position) {
        switch (view.getId()) {


            case R.id.order_list_zt://确认收款


                final MyDialog myDialog = new MyDialog(OrderActivity.this, "是否确认收款?");
                myDialog.show();
                myDialog.positive.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        OrderBean temp1=  (OrderBean) adapter.getData().get(position);
                        if(temp1.getStatus()==0){
                            temp1.setStatus(1);
                            getPresenter().UpdateOrder(temp1,position);
                        }else{
                            Toast.makeText(OrderActivity.this,"不可修改",Toast.LENGTH_LONG).show();
                        }

                        myDialog.dismiss();
                    }
                });
                myDialog.negative.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        myDialog.dismiss();
                    }

                });
                break;


            case R.id.order_btn_phone://打电话
                OrderBean temp=  (OrderBean) adapter.getData().get(position);
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + temp.getPhone());
                intent.setData(data);
                OrderActivity.this.startActivity(intent);
                break;
            case R.id.order_list_qx://取消订单
                final MyDialog myDialogQx = new MyDialog(OrderActivity.this, "是否取消订单?");
                myDialogQx.show();
                myDialogQx.positive.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        OrderBean temp1=  (OrderBean) adapter.getData().get(position);
                        if(temp1.getStatus()!=-1){
                            temp1.setStatus(1);
                            getPresenter().UpdateOrder(temp1,position);
                        }else{
                            Toast.makeText(OrderActivity.this,"订单已取消",Toast.LENGTH_LONG).show();
                        }

                        myDialogQx.dismiss();
                    }
                });
                myDialogQx.negative.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        myDialogQx.dismiss();
                    }

                });
                break;


        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onMessageResponse(Response message) {
        ToastUtils.showLong("新订单提醒");
        System.out.println("新消息提示"+message.getResponseText());

    }

    @Override
    public void onSendMessageError(ErrorResponse error) {
        System.out.println("新消息错误"+error.getResponseText());
    }
}
