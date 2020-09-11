package com.example.xinguannews.entitylist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinguannews.GraphSchemaFragment;
import com.example.xinguannews.R;
import com.example.xinguannews.entity.Entity;
import com.example.xinguannews.entity.EntityRelation;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// 管理新闻列表中的卡片的 Adapter
public class EntityRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_COLLAPSED = 0;
    public static final int VIEW_TYPE_EXPANDED = 1;
    public boolean clickable = true;

    final Context context;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    GraphSchemaFragment fragment;

    public List<Entity> entities;
    public List<Integer> viewTypes = new ArrayList<>();

    public EntityRecyclerViewAdapter(List<Entity> entities, Context context, GraphSchemaFragment fragment) {
        this.entities = entities;
        this.context = context;
        this.fragment = fragment;

        // init other members
        linearLayoutManager = new LinearLayoutManager(context);
        for (Entity e : entities) {
            viewTypes.add(VIEW_TYPE_COLLAPSED);  // set all to collapsed
        }
    }

    public void addEntity(Entity entity, Integer viewType) {
        entities.add(entity);
        if (viewType != null) {
            viewTypes.add(viewType);
        } else {
            viewTypes.add(VIEW_TYPE_COLLAPSED);
        }
        notifyItemInserted(entities.size() - 1);
    }

    public void setEntities(List<Entity> entities) {
        viewTypes.clear();
        this.entities.clear();
        this.entities.addAll(entities);
        for (Entity e : entities) viewTypes.add(VIEW_TYPE_COLLAPSED);
        notifyDataSetChanged();
    }

    public void collapseAll() {
        for (Integer i : viewTypes) {
            i = VIEW_TYPE_COLLAPSED;
        }
        notifyDataSetChanged();
    }

    // inflate a layout from XML, and return a corresponding holder instance;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == VIEW_TYPE_COLLAPSED) {
            // Inflate the custom layout
            View view = inflater.inflate(R.layout.card_entity, parent, false);
            return new ViewHolderCollapsed(view);
        } else {
            View view = inflater.inflate(R.layout.card_entity_expanded, parent, false);
            return new ViewHolderExpanded(view);
        }
    }

    // 给定小标 pos，用对应的 article 的信息给一个 holder 对象赋值
    // 然后 RecyclerView 会自动添加到列表中
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int pos) {
        if (holder instanceof ViewHolderCollapsed) {
            setItemCollapsed((ViewHolderCollapsed) holder, pos);
        } else {
            setItemExpanded((ViewHolderExpanded) holder, pos);
        }
    }

    @Override
    public int getItemCount() {
        return entities == null ? 0 : entities.size();
    }

    @Override
    public int getItemViewType(int pos) {
//        System.out.println("pos: " + pos);
//        System.out.println(entities.size() + " " + viewTypes.size());
        if (pos >= getItemCount()) {
            return VIEW_TYPE_COLLAPSED;
        }
        if (entities.get(pos) == null) {
            return VIEW_TYPE_COLLAPSED;
        }
        return viewTypes.get(pos);
    }

    // ViewHolder 会保持 RecyclerView 列表中的元素的 View 的信息
    // 用于动态地对列表元素进行删除和添加
    public class ViewHolderCollapsed extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textLabel;

        public ViewHolderCollapsed(@NonNull View itemView) {
            super(itemView);
//            System.out.println("called constructor of ViewHolder");
            textLabel = itemView.findViewById(R.id.card_entity_label);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (clickable == false) {
                return;
            }
            int pos = getLayoutPosition();
            Entity entity = entities.get(pos);

            System.out.println("onClick");
            System.out.println(pos);
            System.out.println(entity);
            viewTypes.set(pos, VIEW_TYPE_EXPANDED);
            notifyItemChanged(pos);
//
//            Context context = view.getContext();
//            Intent intent = new Intent(context, EntityActivity.class);
//            intent.putExtra("entity", (Parcelable) entity);
//            context.startActivity(intent);
        }
    }

    // Loading view, at the end of the list
    private class ViewHolderExpanded extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View view;
        public TextView label;
        public TextView baidu;
        public ImageView img;
        public LinearLayout relationsDiv;
        public LinearLayout propertiesDiv;
        public LinearLayout relationsList;
        public LinearLayout propertiesList;
        public View labelDiv;
        private ImageButton buttonCollapse;

        public ViewHolderExpanded(@NonNull View view) {
            super(view);
            this.view = view;
            label = view.findViewById(R.id.card_entity_label);
            baidu = view.findViewById(R.id.card_entity_baidu);
            img = view.findViewById(R.id.card_entity_img);
            relationsDiv = view.findViewById(R.id.card_entity_relations_div);
            relationsList = view.findViewById(R.id.card_entity_relations_list);
            propertiesDiv = view.findViewById(R.id.card_entity_properties_div);
            propertiesList = view.findViewById(R.id.card_entity_properties_list);
            buttonCollapse = view.findViewById(R.id.card_entity_button_collapse);
            labelDiv = view.findViewById(R.id.card_entity_label_div);

            labelDiv.setOnClickListener(this);
            buttonCollapse.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getLayoutPosition();
            Entity entity = entities.get(pos);

            System.out.println("onClick");
            System.out.println(pos);
            System.out.println(entity);
            viewTypes.set(pos, VIEW_TYPE_COLLAPSED);
            notifyItemChanged(pos);
        }
    }

    private void setItemExpanded(ViewHolderExpanded holder, int pos) {
        Entity entity = entities.get(pos);
        TextView label = holder.label;
        TextView baidu = holder.baidu;
        ImageView img = holder.img;
        label.setText(entity.label);
        baidu.setText(entity.baidu);

//        System.out.println("setItemExpanded");
//        System.out.println(entity);
//        System.out.println(entity.relations);
//        System.out.println(entity.properties);

        // relations
        holder.relationsList.removeAllViewsInLayout();
        if (entity.relations == null || entity.relations.isEmpty()) {
            holder.relationsDiv.setVisibility(View.GONE);
        } else {
            holder.relationsDiv.setVisibility(View.VISIBLE);
            for (EntityRelation relation : entity.relations) {
                holder.relationsList.addView(genRelationRow(relation));
            }
        }

        // properties
        holder.propertiesList.removeAllViewsInLayout();
        if (entity.properties == null || entity.properties.isEmpty()) {
//            System.out.println("no properties");
            holder.propertiesDiv.setVisibility(View.GONE);
        } else {
            holder.propertiesDiv.setVisibility(View.VISIBLE);
            for (Map.Entry<String, String> property : entity.properties.entrySet()) {
                holder.propertiesList.addView(genPropertyRow(property));
            }
        }

        // show image in the ImageView
        new DownloadImageTask(img).execute(entity.img);

    }

    private void setItemCollapsed(ViewHolderCollapsed holder, int pos) {
        Entity entity = entities.get(pos);
        TextView textTitle = holder.textLabel;
        textTitle.setText(entity.label);
    }

    private View genPropertyRow(Map.Entry<String, String> property) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_entity_properties_row, null, false);

        TextView key = view.findViewById(R.id.text_card_view_properties_row_key);
        TextView val = view.findViewById(R.id.text_card_view_properties_row_value);

        key.setText(property.getKey());
        val.setText(property.getValue());
        return view;
    }

    private View genRelationRow(final EntityRelation rel) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_entity_relations_card, null, false);

        TextView relation = view.findViewById(R.id.card_entity_relation_relation);
        TextView entity = view.findViewById(R.id.card_entity_relation_entity);
        ImageButton buttonQuery = view.findViewById(R.id.card_entity_relation_query);
        ImageView arrow = view.findViewById(R.id.card_entity_relation_arrow);
        Drawable arrowForward = ContextCompat.getDrawable(context, R.drawable.ic_arrow_forward_white_24dp);
        Drawable arrowBack = ContextCompat.getDrawable(context, R.drawable.ic_arrow_back_white_24dp);
        if (rel.forward == true) {
            arrow.setBackground(arrowForward);
        } else {
            arrow.setBackground(arrowBack);
        }

        relation.setText(rel.relation);
        entity.setText(rel.label);
        buttonQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.onQuery(rel.label);
            }
        });

        return view;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public interface OnSearchRelationListener {
        void onSearchRelation(String entity);
    }
}
