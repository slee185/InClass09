package edu.uncc.inclass09;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import edu.uncc.inclass09.databinding.FragmentPostsBinding;
import edu.uncc.inclass09.databinding.PagingRowItemBinding;
import edu.uncc.inclass09.databinding.PostRowItemBinding;
import edu.uncc.inclass09.models.Post;
import edu.uncc.inclass09.models.PostResponse;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PostsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostsFragment newInstance(String param1, String param2) {
        PostsFragment fragment = new PostsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    FragmentPostsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPostsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.createPost();
            }
        });

        binding.buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.logout();
            }
        });

        binding.recyclerViewPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        postsAdapter = new PostsAdapter();
        binding.recyclerViewPosts.setAdapter(postsAdapter);


        binding.recyclerViewPaging.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        pagingAdapter = new PagingAdapter();
        binding.recyclerViewPaging.setAdapter(pagingAdapter);

        binding.textViewPaging.setText("Loading ...");

        getActivity().setTitle(R.string.posts_label);

        getPosts();
    }

    private final OkHttpClient client = new OkHttpClient();

    void getPosts() {
        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/posts?page=1")
               // .addHeader("Authorization", "BEARER" + mAuth.getToken)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    String body = response.body().string();
                    Gson gson = new Gson();
                    PostResponse postResponse = gson.fromJson(body, PostResponse.class);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPosts.clear();
                            mPosts.addAll(postResponse.getPosts());
                            postsAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    PostsAdapter postsAdapter;
    PagingAdapter pagingAdapter;
    ArrayList<Post> mPosts = new ArrayList<>();
    ArrayList<String> mPages = new ArrayList<>();

    class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsViewHolder> {
        @NonNull
        @Override
        public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            PostRowItemBinding binding = PostRowItemBinding.inflate(getLayoutInflater(), parent, false);
            return new PostsViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull PostsViewHolder holder, int position) {
            Post post = mPosts.get(position);
            holder.setupUI(post);
        }

        @Override
        public int getItemCount() {
            return mPosts.size();
        }

        class PostsViewHolder extends RecyclerView.ViewHolder {
            PostRowItemBinding mBinding;
            Post mPost;
            public PostsViewHolder(PostRowItemBinding binding) {
                super(binding.getRoot());
                mBinding = binding;
            }

            public void setupUI(Post post){
                mPost = post;
                mBinding.textViewPost.setText(post.getPost_text());
                mBinding.textViewCreatedBy.setText(post.getCreated_by_name());
                mBinding.textViewCreatedAt.setText(post.getCreated_at());
                mBinding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }

    }

    class PagingAdapter extends RecyclerView.Adapter<PagingAdapter.PagingViewHolder> {
        @NonNull
        @Override
        public PagingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            PagingRowItemBinding binding = PagingRowItemBinding.inflate(getLayoutInflater(), parent, false);
            return new PagingViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull PagingViewHolder holder, int position) {
            String page = mPages.get(position);
            holder.setupUI(page);
        }

        @Override
        public int getItemCount() {
            return mPages.size();
        }

        class PagingViewHolder extends RecyclerView.ViewHolder {
            PagingRowItemBinding mBinding;
            String mPage;
            public PagingViewHolder(PagingRowItemBinding binding) {
                super(binding.getRoot());
                mBinding = binding;
            }

            public void setupUI(String page){
                mPage = page;
                mBinding.textViewPageNumber.setText(page);
                mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }

    }

    PostsListener mListener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (PostsListener) context;
    }

    interface PostsListener{
        void logout();
        void createPost();
    }
}