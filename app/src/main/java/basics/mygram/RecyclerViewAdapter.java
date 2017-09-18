package basics.mygram;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anthonyivan on 13/09/17.
 */

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.viewHolder>{

    Context context;
    List<ParseObject> listParseObject;

    public RecyclerViewAdapter(Context context, List<ParseObject> listParseObject) {
        this.context = context;
        this.listParseObject = listParseObject;
    }

    @Override
    public RecyclerViewAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, null);
        viewHolder holder = new viewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.viewHolder holder, int position) {
        final ParseObject obj = listParseObject.get(position);

        if(obj.getParseFile("image").getUrl() != ""){
            Picasso.with(context)
                    .load(obj.getParseFile("image").getUrl())
                    .into(holder.imageViewThumbnail);
        }
        holder.textViewDate.setText(obj.getCreatedAt().toString());
        holder.textViewCaption.setText(obj.getString("caption"));
//        holder.textViewCaption.setText(obj.getParseFile("image").getUrl());

    }

    @Override
    public int getItemCount() {
        return listParseObject.size();
    }

    public void setList(List<ParseObject> list) {
        listParseObject = new ArrayList<>();
        listParseObject = list;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        ImageView imageViewThumbnail;
        TextView textViewDate;
        TextView  textViewCaption;

        public viewHolder(View itemView) {
            super(itemView);
            imageViewThumbnail = (ImageView) itemView.findViewById(R.id.image_thumbnail);
            textViewDate = (TextView) itemView.findViewById(R.id.text_date);
            textViewCaption = (TextView) itemView.findViewById(R.id.text_caption);

        }
    }
}
