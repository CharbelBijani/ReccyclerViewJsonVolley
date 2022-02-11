package com.example.reccyclerviewjsonvolley;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class AdapterItem extends RecyclerView.Adapter<AdapterItem.ItemViewHolder> {
    /**
     * #4 Ajouts des variables poour le constructeur de notre Adapter soit le context et un tableau pour
     * enregistrer les données provenant du fichier JSON
     **/
    private Context context;
    private ArrayList<ModelItem> itemArrayList;

    /**
     * #10 Ajout de l'interface pour gèrer le clic
     **/
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    /**
     * #10.1 Ajout d'un objet de type OnItemClickListener pour passer au setOnCLickListener que l'on appellera en cliquant sur une ligne
     **/
    private OnItemClickListener mOnItemClickListener;

    /**
     * #10.2 Création du setOnItemClickListener
     **/
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
    // Il ne reste plus qu'a ajouter ce listener dans la classe ItemielwHolder

    /**
     * #5 Ajout du constructeur, basé sur les deux variables ci-dessus
     **/
    public AdapterItem(Context context, ArrayList<ModelItem> itemArrayList) {
        this.context = context;
        this.itemArrayList = itemArrayList;
    }

    /**
     * #1 On ajoute les méthodes r le bon focntiopnnemnt de notre classe avec [ALT] + [ENTRER]
     **/
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /** #7 On ajoute la vue dans laquelle on va inflate les données **/
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        /** #8 On va remplir le recycler avec les données en fonction de la position dans le ArrayList et le Recycler **/
        /** #8.1 Création d'une variable pour récupérer la position dans le tableau **/
        ModelItem currentItem = itemArrayList.get(position);

        /** #8.2 On utilise le model pour récupérer les données qui nous intéresse **/
        String imageUrl = currentItem.getImageUrl();
        String creator = currentItem.getCreator();
        int likes = currentItem.getLikes();

        /** #8.3 On associe les données récupérées avec le holder de vue **/
        holder.tvCreator.setText(creator);
        holder.tvLikes.setText("Likes : " + likes);

        /** #8.4 On affiche les images avec l'url **/
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher);

        Context context = holder.ivImageView.getContext();
        Glide.with(context)
                .load(imageUrl)
                .apply(options)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivImageView);
        /** #9 L'adapter est fini retournons dans le MainActivity pour récupérer les données via le fichier JSON **/
    }

    /**
     * #6 On commence par le plus simple
     **/
    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        /**
         * #3 On ajoute les variables des différents widgets du layout item_recycler pour les lier dans le code
         **/
        public ImageView ivImageView;
        public TextView tvCreator, tvLikes;

        /**
         * #2 On ajoute les dépendances de la classe ViewHolder puis on ajote le constructeur avec [ALT] + [ENTRER]
         **/
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImageView = itemView.findViewById(R.id.ivImageView);
            tvCreator = itemView.findViewById(R.id.tvCreator);
            tvLikes = itemView.findViewById(R.id.tvLikes);

            /** #10.2 Ajout du listener sur toute la ligne **/
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnItemClickListener != null) {
                        int pos = getBindingAdapterPosition();
                        if( pos != RecyclerView.NO_POSITION){
                            mOnItemClickListener.onItemClick(pos);
                        }
                    }
                }
            }); // Implémenter cette interface dans MainActivity
        }
    }
}
