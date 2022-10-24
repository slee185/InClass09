package edu.uncc.inclass09;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import edu.uncc.inclass09.databinding.FragmentLoginBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginFragment extends Fragment {

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentLoginBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private final OkHttpClient client = new OkHttpClient();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.editTextEmail.setText("a@l.com");
        binding.editTextPassword.setText("test123");

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.editTextEmail.getText().toString();
                String password = binding.editTextPassword.getText().toString();
                if(email.isEmpty()){
                    Toast.makeText(getActivity(), "Enter valid email!", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()){
                    Toast.makeText(getActivity(), "Enter valid password!", Toast.LENGTH_SHORT).show();
                } else {

                   RequestBody formBody = new FormBody.Builder()
                           .add("email", email)
                           .add("password", password)
                           .build();

                   Request request = new Request.Builder()
                           .url("https://www.theappsdr.com/posts/login")
                           .post(formBody)
                           .build();

                  client.newCall(request).enqueue(new Callback() {
                      @Override
                      public void onFailure(@NonNull Call call, @NonNull IOException e) {
                          getActivity().runOnUiThread(new Runnable() {
                              @Override
                              public void run() {
                                  Toast.makeText(requireActivity(), "Unable to access the logins.", Toast.LENGTH_LONG).show();
                              }
                          });
                      }

                      @Override
                      public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                          if (!response.isSuccessful()) {
                              Toast.makeText(requireActivity(), "Unable to access the logins.", Toast.LENGTH_LONG).show();
                          } else {
                              Gson gson = new Gson();

                          }
                      }
                  });

                }
            }
        });

        binding.buttonCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.createNewAccount();
            }
        });

        getActivity().setTitle(R.string.login_label);
    }

    LoginListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (LoginListener) context;
    }

    interface LoginListener {
        void createNewAccount();
    }
}