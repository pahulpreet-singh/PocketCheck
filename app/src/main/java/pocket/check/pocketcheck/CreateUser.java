package pocket.check.pocketcheck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateUser extends AppCompatActivity {


    Button create;
    EditText name,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        create= (Button) findViewById(R.id.button);
        name=(EditText)findViewById(R.id.editText);
        email=(EditText)findViewById(R.id.editText2);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();


            }
        });

    }



    void insert()
    {

        if(name.getText().toString().equals("") || email.getText().toString().equals(""))
        {
            Toast.makeText(getBaseContext(),"Enter the values",Toast.LENGTH_LONG).show();
        }
        else {
            Dbclass obj = new Dbclass(getApplicationContext());

            obj.caller();
            long status = -1;

            status = obj.createUser(name.getText().toString(), email.getText().toString());

            if (status > 0) {

                Toast.makeText(getApplicationContext(), "New user '" + name.getText().toString()
                        + "' created successfully", Toast.LENGTH_SHORT).show();
                Intent next = new Intent(getBaseContext(), Home.class);
                finish();
                startActivity(next);

            } else {
                Toast.makeText(getApplicationContext(), "Error. Please contact the developer.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
