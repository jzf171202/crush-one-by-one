package com.zjrb.sjzsw.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.zjrb.sjzsw.greendao.DaoSession;
import com.zjrb.sjzsw.greendao.GreenDaoTest2Dao;
import com.zjrb.sjzsw.greendao.GreenDaoTestDao;

/**
 * Created by Thinkpad on 2017/11/3.
 */
@Entity
public class GreenDaoTest {
    @Id
    private Long id;
    private String name;
    @ToMany(referencedJoinProperty = "pid")
    private List<GreenDaoTest2> greenDaoTest2List;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1289396823)
    private transient GreenDaoTestDao myDao;
    @Generated(hash = 842460553)
    public GreenDaoTest(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    @Generated(hash = 456386966)
    public GreenDaoTest() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1570401068)
    public List<GreenDaoTest2> getGreenDaoTest2List() {
        if (greenDaoTest2List == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GreenDaoTest2Dao targetDao = daoSession.getGreenDaoTest2Dao();
            List<GreenDaoTest2> greenDaoTest2ListNew = targetDao
                    ._queryGreenDaoTest_GreenDaoTest2List(id);
            synchronized (this) {
                if (greenDaoTest2List == null) {
                    greenDaoTest2List = greenDaoTest2ListNew;
                }
            }
        }
        return greenDaoTest2List;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 313944998)
    public synchronized void resetGreenDaoTest2List() {
        greenDaoTest2List = null;
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 729543882)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGreenDaoTestDao() : null;
    }
}
