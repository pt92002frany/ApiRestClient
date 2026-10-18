package edu.sv.ues.dam235.apirestclient.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.sv.ues.dam235.apirestclient.R;
import edu.sv.ues.dam235.apirestclient.models.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> products;

    // Constructor que recibe la lista de productos desde la Activity
    public ProductAdapter(List<Product> products) {
        this.products = products;
    }

    // 1. Inflamos el layout del item (item_product.xml)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    // 2. Asignamos los datos a las vistas de cada item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product p = products.get(position);
        holder.tvCode.setText("Código: " + p.getCode());
        holder.tvName.setText("Nombre: " + p.getName());
        holder.tvStatus.setText("Estado: " + (p.isStatus() ? "Activo" : "Inactivo"));
    }

    // 3. Cuántos items hay en total
    @Override
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }

    // Clase interna que hace referencia a las vistas del layout
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCode, tvName, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCode = itemView.findViewById(R.id.tvCode);
            tvName = itemView.findViewById(R.id.tvName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}