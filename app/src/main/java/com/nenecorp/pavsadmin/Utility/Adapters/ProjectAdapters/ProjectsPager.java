package com.nenecorp.pavsadmin.Utility.Adapters.ProjectAdapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.nenecorp.pavsadmin.DataModel.Pavs.Project;
import com.nenecorp.pavsadmin.Interface.AdminUi.DownloadProjects;
import com.nenecorp.pavsadmin.R;
import com.nenecorp.pavsadmin.Utility.Resources.Animator;
import com.nenecorp.pavsadmin.Utility.Resources.Dictionary;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ProjectsPager extends PagerAdapter {
    private Context context;
    private ArrayList<Project> listItems;
    private Boolean downloadBtn;
    private ProjectSelected projectSelected;

    public interface ProjectSelected {
        void projectResult(Project project);
    }

    public ProjectsPager(Context context, ArrayList<Project> listItems) {
        this.context = context;
        this.listItems = listItems;
        this.downloadBtn = false;
    }

    public ProjectsPager(DownloadProjects downloadProjects, ArrayList<Project> projects, ProjectSelected projectSelected) {
        this.context = downloadProjects;
        this.listItems = projects;
        this.projectSelected = projectSelected;
        this.downloadBtn = true;
    }

    @NotNull
    @Override
    public Object instantiateItem(@NotNull ViewGroup container, int position) {
        View parentView = LayoutInflater.from(context).inflate(R.layout.page_item_project, null);
        try {
            Project x = listItems.get(position);

            container.addView(parentView);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return parentView;
    }

    public Project getProject(int id) {
        return listItems.get(id);

    }


    @Override
    public void destroyItem(ViewGroup container, int position, @NotNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public boolean isViewFromObject(@NotNull View view, @NotNull Object object) {
        return (view == object);
    }

}