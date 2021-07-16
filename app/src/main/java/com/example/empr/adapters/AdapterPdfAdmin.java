package com.example.empr.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.empr.MyApplication;
import com.example.empr.author.PdfDetailActivity;
import com.example.empr.author.PdfEditActivity;
import com.example.empr.databinding.RowPdfListAdminBinding;
import com.example.empr.filter.FilterPdfAdmin;
import com.example.empr.models.ModelPdf;
import com.github.barteksc.pdfviewer.PDFView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdapterPdfAdmin extends RecyclerView.Adapter<AdapterPdfAdmin.HolderPdfAdmin> implements Filterable {

    //context
    private Context context;
    //arraylist to hold list of data of type ModelPdf
    public ArrayList<ModelPdf> pdfArrayList, filterList;

    //view binding row_pdf_admin.xml
    private RowPdfListAdminBinding binding;

    private FilterPdfAdmin filter;

    private static final String TAG = "PDF_ADAPTER_TAG";


    //constructor
    public AdapterPdfAdmin(Context context, ArrayList<ModelPdf> pdfArrayList) {
        this.context = context;
        this.pdfArrayList = pdfArrayList;
        this.filterList = pdfArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public HolderPdfAdmin onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        //bind layout
        binding = RowPdfListAdminBinding.inflate(LayoutInflater.from(context), parent, false);

        return new HolderPdfAdmin(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterPdfAdmin.HolderPdfAdmin holder, int position) {
        /*get data, set data, handle clicks etc.*/

        //get data
        ModelPdf model = pdfArrayList.get(position);
        String pdfId = model.getId();
        String categoryId = model.getCategoryId();
        String title = model.getTitle();
        String description = model.getDescription();
        String pdfUrl = model.getUrl();
        String timestamp = model.getTimestamp();
        long longtime = Long.parseLong(timestamp);

        //we need to convert timestamp to dd/mm/yyyy format
        String formattedDate = MyApplication.formatTimestamp(longtime);

        //set data
        holder.titleTv.setText(title);
        holder.descriptionTv.setText(description);
        holder.dateTv.setText(formattedDate);

        //load further details like category, pdf from url, pdf size
        MyApplication.loadCategory(
                "" + categoryId,
                holder.categoryTv
        );
        MyApplication.loadPdfFromUrlSinglePage(
                "" + pdfUrl,
                "" + title,
                holder.pdfView,
                holder.progressBar
        );
        MyApplication.loadPdfSize(
                "" + pdfUrl,
                "" + title,
                holder.sizeTv
        );

        //handle click, show dialog with options 1) edit, 2) delete
        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreOptionsDialog(model, holder);
                
            }
        });

        //handle book/pdf click, open pdf details page, pass pdf/book id to get details of it
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PdfDetailActivity.class);
                intent.putExtra("bookId", pdfId);
                context.startActivity(intent);
            }
        });
    }

    private void moreOptionsDialog(ModelPdf model, HolderPdfAdmin holder) {
        String bookId = model.getId();
        String bookUrl = model.getUrl();
        String bookTitle = model.getTitle();

        //options to show in dialog
        String[] options = {"Edit", "Delete"};

        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose Options")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //handle dialog option click
                        if (which == 0){
                            //Edit clicked, open PdfEditActivity to edit the book info
                            Intent intent = new Intent(context, PdfEditActivity.class);
                            intent.putExtra("bookId", bookId);
                            context.startActivity(intent);

                        }
                        else if (which == 1){
                            //delete clicked
                            MyApplication.deleteBook(
                                    context,
                                    "" + bookId,
                                    "" + bookUrl,
                                    "" + bookTitle);
                        }
                    }
                })
                .show();
    }

    @Override
    public int getItemCount() {
        return pdfArrayList.size(); //return number of records / list size
    }

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter = new FilterPdfAdmin(filterList, this);

        }
        return filter;
    }

    /*View Holder class for row_pdf_admin.xml*/
    class HolderPdfAdmin extends RecyclerView.ViewHolder{

        //ui views of row_pdf_admin.xml
        PDFView pdfView;
        ProgressBar progressBar;
        TextView titleTv, descriptionTv, categoryTv, sizeTv, dateTv;
        ImageButton moreBtn;

        public HolderPdfAdmin(@NonNull @NotNull View itemView) {
            super(itemView);

            //init ui views
            pdfView = binding.pdfView;
            progressBar = binding.progressBar;
            titleTv = binding.titleTv;
            descriptionTv = binding.descriptionTv;
            categoryTv = binding.categoryTv;
            sizeTv = binding.sizeTv;
            dateTv = binding.dateTv;
            moreBtn = binding.moreBtn;
        }
    }
}
