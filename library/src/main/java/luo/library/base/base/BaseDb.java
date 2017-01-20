package luo.library.base.base;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库操作
 */

public class BaseDb {
    public static DbManager db;

    /**
     * 本地数据的初始化
     */
    public static void initDb() {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("xutils3_db") //设置数据库名
                .setDbVersion(1) //设置数据库版本
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        db.getDatabase().enableWriteAheadLogging();
                        //开启WAL, 对写入加速提升巨大(作者原话)
                    }
                })
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        //数据库升级操作
                    }
                });
        db = x.getDb(daoConfig);
    }

    /**
     * 添加数据
     */
    public static boolean add(Object entity) {
        try {
            db.save(entity);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除数据
     */
    public static boolean delete(Object entity) {
        try {
            db.delete(entity);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 修改数据
     */
    public static boolean update(Object entity) {
        try {
            db.update(entity);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查找数据
     */
    public static <T> List<T> find(Class<T> cls) {
        List<T> list = new ArrayList<>();
        try {
            list = db.findAll(cls);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

}
