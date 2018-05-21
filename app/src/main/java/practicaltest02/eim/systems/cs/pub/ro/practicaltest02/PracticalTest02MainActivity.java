package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02MainActivity extends AppCompatActivity {

    Button setButton, resetButton, pollButton, startServerButton;
    EditText clientAddressEditText, clientPortEditText, oraEditText, minutEditText;
    TextView responseTextView;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;


    private StartServerButtonClickListener startServerButtonListener = new StartServerButtonClickListener();
    private class StartServerButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = clientPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }

    }

    private PollAlarmButtonClickListener pollAlarmButtonClickListener = new PollAlarmButtonClickListener();
    private class PollAlarmButtonClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            String clientAddress = clientAddressEditText.getText().toString();
            String clientPort = clientPortEditText.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty()
                    || clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
            Integer ora = Integer.parseInt(oraEditText.getText().toString());
            Integer minut = Integer.parseInt(minutEditText.getText().toString());
            if (ora == null || minut == null) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            clientThread = new ClientThread(
                    clientAddress, Integer.parseInt(clientPort), responseTextView
            );
            clientThread.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        setButton = findViewById(R.id.set_timer_button);
        resetButton = findViewById(R.id.reset_timer_button);
        pollButton = findViewById(R.id.poll_timer_button);
        pollButton.setOnClickListener(pollAlarmButtonClickListener);

        startServerButton = findViewById(R.id.start_server_button);
        startServerButton.setOnClickListener(startServerButtonListener);

        clientAddressEditText = findViewById(R.id.client_address_edit_text);
        clientPortEditText = findViewById(R.id.client_port_edit_text);

        oraEditText = findViewById(R.id.ora_edit_text);
        minutEditText = findViewById(R.id.minut_edit_text);

        responseTextView = findViewById(R.id.response_text_view);

    }

    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}
