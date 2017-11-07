package com.zjrb.sjzsw.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zjrb.sjzsw.App;
import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.entity.GreenDaoTest;
import com.zjrb.sjzsw.entity.GreenDaoTest2;
import com.zjrb.sjzsw.greendao.DaoMaster;
import com.zjrb.sjzsw.greendao.DaoSession;
import com.zjrb.sjzsw.greendao.GreenDaoTest2Dao;
import com.zjrb.sjzsw.greendao.GreenDaoTestDao;
import com.zjrb.sjzsw.greendao.MySQLiteOpenHelper;

import java.util.List;

/**
 * Created by Thinkpad on 2017/11/3.
 */

public class GreenDaoTestActivity extends BaseActivity{

    private GreenDaoTestDao greenDaoTestDao;
    private GreenDaoTest2Dao greenDaoTest2Dao;

    private List<GreenDaoTest> greenDaoTestList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_newsdetail;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {


        initGreenDao();
        test();

    }



    private void initGreenDao() {
        greenDaoTestDao = App.getAppContext().getDaoSession().getGreenDaoTestDao();
        greenDaoTest2Dao = App.getAppContext().getDaoSession().getGreenDaoTest2Dao();



    }

    private void test() {

//        GreenDaoTest greenDaoTest = new GreenDaoTest();
//        greenDaoTest.setId(3L);
//        greenDaoTest.setName("一级表3");
//        greenDaoTestDao.insert(greenDaoTest);
//
//        GreenDaoTest2 greenDaoTest2 = new GreenDaoTest2();
//        greenDaoTest2.setId(4L);
//        greenDaoTest2.setName("二级表4");
//        greenDaoTest2.setPid(3L);
//        greenDaoTest2Dao.insert(greenDaoTest2);

        greenDaoTestList = greenDaoTestDao.loadAll();

        for(GreenDaoTest gg : greenDaoTestList)
        {
            Log.d("greendao", gg.getId() + gg.getName());
            List<GreenDaoTest2> greenDaoTest2List = gg.getGreenDaoTest2List();
            for(GreenDaoTest2 g2 : greenDaoTest2List)
            {
                Log.d("greendao", g2.getId() + g2.getName() + g2.getPid());
            }
        }



    }
}
