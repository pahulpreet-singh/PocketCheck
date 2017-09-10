package pocket.check.pocketcheck;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by multani on 24/01/16.
 */
public class Dbclass extends SQLiteOpenHelper
{

    Context con;
    static final String DB_NAME = "pocketcheckdb";
    static final int VERSION = 1;
    SQLiteDatabase dbcall;

    Dbclass(Context con1)
    {
        super(con1,DB_NAME,null,VERSION);
        con=con1;

    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        //new user
        String createUserQ="create table if not exists 'users'('srno' integer primary key AUTOINCREMENT" +
                ", 'name' varchar(100), 'email' varchar(100))";

        //new account
        String addAccountQ="create table if not exists 'accounts'('srno' integer primary key autoincrement" +
                ",'name' varchar(100), 'type' varchar(100), 'balance' BIGINT," +
                "'include' varchar(10),'defaultt' varchar(10))";
        //default account on create
        String accountCashQ="insert into 'accounts'(name,type,balance,defaultt) values('Cash','Cash'," +
                "'0','true')";

        //new income
        String addIncomeQ="create table if not exists 'incomes'('srno' integer primary key autoincrement," +
                "'amount' BIGINT, 'accName' varchar(100), 'accSrno' integer" +
                ",'label' varchar(100),'date' varchar(20),FOREIGN KEY ('accSrno') REFERENCES accounts(srno))";

        //total balance
        String balanceQ="create table if not exists 'balanceTable'('srno' integer primary key," +
                "'balance' BIGINT)";
        String initBalanceQ="insert into 'balanceTable'(srno,balance) values('1','0')";

        //new expense
        String expQ1="create table if not exists 'mon1'('srno' integer primary key autoincrement," +
                "'expense' BIGINT,'expType' varchar(30)," +
                "'expSubtype' varchar(30),'accName' varchar(100),'notes' varchar(100),'date' varchar(20))";
        String expQ2="create table if not exists 'mon2'('srno' integer primary key autoincrement," +
                "'expense' BIGINT,'expType' varchar(30)," +
                "'expSubtype' varchar(30),'accName' varchar(100),'notes' varchar(100),'date' varchar(20))";
        String expQ3="create table if not exists 'mon3'('srno' integer primary key autoincrement," +
                "'expense' BIGINT,'expType' varchar(30)," +
                "'expSubtype' varchar(30),'accName' varchar(100),'notes' varchar(100),'date' varchar(20))";
        String expQ4="create table if not exists 'mon4'('srno' integer primary key autoincrement," +
                "'expense' BIGINT,'expType' varchar(30)," +
                "'expSubtype' varchar(30),'accName' varchar(100),'notes' varchar(100),'date' varchar(20))";

        try{
            db.execSQL(createUserQ);
            db.execSQL(addAccountQ);
            db.execSQL(accountCashQ);
            db.execSQL(addIncomeQ);
            db.execSQL(balanceQ);
            db.execSQL(initBalanceQ);
            db.execSQL(expQ1);
            db.execSQL(expQ2);
            db.execSQL(expQ3);
            db.execSQL(expQ4);

            //Toast.makeText(con,"Created",Toast.LENGTH_LONG).show();
        }
        catch(Exception e)
        {
            Toast.makeText(con,"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
        }



    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {



    }

    public void caller()
    {
        try
        {

            dbcall=this.getWritableDatabase();

        }
        catch(Exception e)
        {
            Toast.makeText(con,"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    public long createUser(String name,String email)
    {

        ContentValues cv=new ContentValues();
        cv.put("name",name);
        cv.put("email",email);

        return dbcall.insert("users",null,cv);

    }
    public long addAccount(String name,String type,long balance,String include,String defaultt)
    {

        ContentValues cv=new ContentValues();
        cv.put("name",name);
        cv.put("type",type);
        cv.put("balance",balance);
        cv.put("include",include);
        cv.put("defaultt",defaultt);

        return dbcall.insert("accounts",null,cv);

    }
    public long addIncome(long amount,String accName,int accSrno,String label,String date)
    {

        ContentValues cv=new ContentValues();
        cv.put("amount",amount);
        cv.put("accName",accName);
        cv.put("accSrno",accSrno);
        cv.put("label",label);
        cv.put("date",date);

        return dbcall.insert("incomes",null,cv);

    }

    public long balance(long bal)
    {
        ContentValues cv=new ContentValues();
        cv.put("balance", bal);
        return dbcall.update("balanceTable", cv, null, null);
    }
    public long addExp(String tableName,long expense,String expType,String expSubtype,String accName,
                       String notes,String date)
    {
        ContentValues cv=new ContentValues();
        cv.put("expense",expense);
        cv.put("expType",expType);
        cv.put("expSubtype",expSubtype);
        cv.put("accName",accName);
        cv.put("notes",notes);
        cv.put("date",date);

        return dbcall.insert(tableName, null, cv);

    }
    public long deleteInc(String srno)
    {
        String where="srno='"+srno+"'";
        return dbcall.delete("incomes", where, null);
    }
    public long deleteExp(String srno,String table)
    {
        String where="srno='"+srno+"'";
        return dbcall.delete(table, where, null);
    }


}
