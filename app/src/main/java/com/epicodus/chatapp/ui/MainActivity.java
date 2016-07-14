package com.epicodus.chatapp.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.epicodus.chatapp.Constants;
import com.epicodus.chatapp.R;
import com.epicodus.chatapp.models.Message;
import com.epicodus.chatapp.models.User;
import com.epicodus.chatapp.ui.LoginActivity;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    final ArrayList<String> messages = new ArrayList<>();

    @Bind(R.id.newMessageButton) Button mNewMessageButton;
    @Bind(R.id.messageEditText) EditText mMessageEditText;
    @Bind(R.id.messageListView) ListView mMessageListView;
    private DatabaseReference mInputtedMessageReference;

    private FirebaseListAdapter mAdapter;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        mAdapter = new FirebaseListAdapter<Message>(this, Message.class, android.R.layout.two_line_list_item, ref) {
            @Override
            protected void populateView(View view, Message message, int position) {
                ((TextView)view.findViewById(android.R.id.text1)).setText(message.getUser());
                ((TextView)view.findViewById(android.R.id.text2)).setText(message.getMessage());

            }
        };
        mMessageListView.setAdapter(mAdapter);

        mInputtedMessageReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(Constants.FIREBASE_CHILD_MESSAGE);


        mInputtedMessageReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("onChildAdded", "called");
                messages.add(dataSnapshot.getValue(Message.class).getMessage());
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                messages.remove(dataSnapshot.getValue(Message.class).getMessage());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mNewMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = mMessageEditText.getText().toString();
                saveMessageToFirebase(message);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void saveMessageToFirebase(String message) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser fUser = mAuth.getCurrentUser();
        String userObject = fUser.getEmail();
        Message messageObject = new Message(message, userObject);
        DatabaseReference ref = mInputtedMessageReference.push();
        String pushId = ref.getKey();
        messageObject.setPushId(pushId);
        ref.setValue(messageObject);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }
}
