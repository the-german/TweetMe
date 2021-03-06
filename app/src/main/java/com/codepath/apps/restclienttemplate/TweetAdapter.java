package com.codepath.apps.restclienttemplate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;



public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{

    public List<Tweet> mTweets;
    Context context;
    private TweetAdapterListener mListener;

    // Define interface requiered by  the viewHolder
    public interface TweetAdapterListener{
        public void onItemSelected(View view, int position);

    }
    //pass in the tweets array in the constructor
    public TweetAdapter(List<Tweet>tweets, TweetAdapterListener listener ){
        mTweets = tweets;
        mListener = listener;

    }
    //fo each row inflate the layout cache references into view holders

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        AtomicReference<View> tweetView = new AtomicReference<>(inflater.inflate(R.layout.item_tweet, parent, false));
        AtomicReference<ViewHolder> viewHolder = new AtomicReference<>(new ViewHolder(tweetView.get()));
        return viewHolder.get();
    }


    //bind the the values based on the position of the element

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tweet tweet = mTweets.get(position);

        holder.tvUserName.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvScrName.setText("@"+tweet.user.screenName);
        holder.tvDAte.setText(getRelativeTimeAgo(tweet.createdAt));

        Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ivProfileImage);
        Glide.with(context).load(tweet.photoUrls).into(holder.ivMedia);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }
    //create the viw holder

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvBody;
        public TextView tvScrName;
        public TextView tvDAte;
        public ImageView ivMedia;


        @SuppressLint("WrongViewCast")
        private ViewHolder(View itemView){
            super(itemView);

            // perform findViewById lookups

            ivProfileImage = itemView.findViewById(R.id.ivProfilImage);
            tvUserName =  itemView.findViewById(R.id.tvUserName);
            tvBody = itemView.findViewById(R.id.tvBody);
          tvScrName = itemView.findViewById(R.id.tvScreenName);
           tvDAte = itemView.findViewById(R.id.tvdate);
           ivMedia = itemView.findViewById(R.id.ivMediaPhoto);

        // handle row click event
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null) {
                        // get position of row element
                        int position = getAdapterPosition();
                        //fire the listener call back
                        mListener.onItemSelected(v,position);
                    }
                }
            });

        }

    }
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
