package pocket.check.pocketcheck;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class Splash extends AppCompatActivity {


    SQLiteDatabase dbcall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Thread()
        {
            public void run()
            {
                try
                {

                    Thread.sleep(700);

                }catch(Exception e)
                {
                    Toast.makeText(getBaseContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
                }
                finally {
                    fetchUser();

                }

            }
        }.start();

    }
    void fetchUser()
    {

        Dbclass obj=new Dbclass(getApplicationContext());

        String q="select * from users";
        dbcall=obj.getReadableDatabase();
        try {
            Cursor result = dbcall.rawQuery(q, null);
            if (result != null) {
                if(result.moveToFirst())
                {
                    Intent home = new Intent(getBaseContext(), Home.class);
                    finish();
                    startActivity(home);
                }
                else {
                    Intent home = new Intent(getBaseContext(), CreateUser.class);
                    finish();
                    startActivity(home);
                }
            }
            else {
                Intent home = new Intent(getBaseContext(), CreateUser.class);
                finish();
                startActivity(home);
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }


    }



}
