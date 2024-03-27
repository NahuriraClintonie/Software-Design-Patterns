package org.IntegratedHealthFinance.systems.views.charts;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.IntegratedHealthFinance.Dao.SaccoDao;
import org.IntegratedHealthFinance.systems.enumerations.Gender;
import org.IntegratedHealthFinance.systems.enumerations.Status;
import org.IntegratedHealthFinance.systems.models.Members;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.donut.DonutChartDataSet;
import org.primefaces.model.charts.donut.DonutChartModel;
import org.primefaces.model.charts.donut.DonutChartOptions;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;

@ManagedBean(name = "chartJsView")
@RequestScoped
public class ChartJsView implements java.io.Serializable {
    private PieChartModel pieModel;
    private DonutChartModel donutModel;
    private BarChartModel ageDistributionModel;
    private DonutChartModel donutModel2;
    private LineChartModel lineModel;

    
    private List<Members> approvedMembers;
    private List<Members> members;

    public List<Members> getMembers() {
        return members;
    }

    public void setMembers(List<Members> members) {
        this.members = members;
    }

    public List<Members> getApprovedMembers() {
        return approvedMembers;
    }

    public void setApprovedMembers(List<Members> approvedMembers) {
        this.approvedMembers = approvedMembers;
    }

    public PieChartModel getPieModel() {
        return pieModel;
    }

    public void setPieModel(PieChartModel pieModel) {
        this.pieModel = pieModel;
    }

    public DonutChartModel getDonutModel2() {
        return donutModel2;
    }

    public void setDonutModel2(DonutChartModel donutModel2) {
        this.donutModel2 = donutModel2;
    }

    public LineChartModel getLineModel() {
        return lineModel;
    }

    public void setLineModel(LineChartModel lineModel) {
        this.lineModel = lineModel;
    }


    @PostConstruct
    public void init() {
        SaccoDao saccoDao = new SaccoDao();
        members = saccoDao.allMembers();
        approvedMembers = saccoDao.getApprovedMembers();
        createPieModel();
        createDonutModel();
        createAgeDistributionModel();
        
    }

    private void createPieModel() {
        pieModel = new PieChartModel();
        ChartData data = new ChartData();

        PieChartDataSet dataSet = new PieChartDataSet();
        List<Number> values = new ArrayList<>();

        int maleCount = 0;
        int femaleCount = 0;

        // Iterate over the approvedMembers list to count genders
        for (Members member : approvedMembers) {
            if (member.getGender()== Gender.Male) {
                maleCount++;
            } else if (member.getGender()==Gender.Female) {
                femaleCount++;
            }
        }

        // Add the counts to the pie chart dataset
        values.add(maleCount);
        values.add(femaleCount);
        dataSet.setData(values);

        // Set background colors for male and female slices in the chart
        List<String> bgColors = new ArrayList<>();
        bgColors.add("rgb(54, 162, 235)"); // Blue for Male
        bgColors.add("rgb(255, 99, 132)"); // Red for Female
        dataSet.setBackgroundColor(bgColors);

        data.addChartDataSet(dataSet);

        // Set the labels for male and female slices in the chart
        List<String> labels = new ArrayList<>();
        labels.add("Male");
        labels.add("Female");
        data.setLabels(labels);

        pieModel.setData(data);
    }

    public void createDonutModel() {
        donutModel = new DonutChartModel();
        ChartData data = new ChartData();
        DonutChartOptions options = new DonutChartOptions();
        // options.setMaintainAspectRatio(false);
        donutModel.setOptions(options);

        DonutChartDataSet dataSet = new DonutChartDataSet();
        List<Number> values = new ArrayList<>();

        // Calculate the count of approved, rejected, and pending members
        int approvedCount = 0;
        int rejectedCount = 0;
        int pendingCount = 0;

        for (Members member : members) {
            Status status = member.getStatus(); // Assuming you have a getStatus() method in Members
            if (status == Status.Approved) {
                approvedCount++;
            } else if (status == Status.Rejected) {
                rejectedCount++;
            } else if (status == Status.Pending) {
                pendingCount++;
            }
        }

        values.add(approvedCount);
        values.add(rejectedCount);
        values.add(pendingCount);
        dataSet.setData(values);

        List<String> bgColors = new ArrayList<>();
        bgColors.add("rgb(0, 123, 255)"); // Blue for Approved
        bgColors.add("rgb(255, 99, 132)"); // Red for Rejected
        bgColors.add("rgb(255, 205, 86)"); // Yellow for Pending
        dataSet.setBackgroundColor(bgColors);

        data.addChartDataSet(dataSet);
        List<String> labels = new ArrayList<>();
        labels.add("Approved");
        labels.add("Rejected");
        labels.add("Pending");
        data.setLabels(labels);

        donutModel.setData(data);
    }

    public void createAgeDistributionModel() {
        ageDistributionModel = new BarChartModel();
        ChartData data = new ChartData();
        ageDistributionModel.setData(data);

        BarChartDataSet dataSet = new BarChartDataSet();
        List<Number> values = new ArrayList<>();

        // Get the age distribution data from your DAO
        SaccoDao saccoDao = new SaccoDao();
        List<Integer> ageDistribution = saccoDao.getAgeDistribution();

        values.addAll(ageDistribution);
        dataSet.setData(values);

        // Assuming you want varying colors for the bars
        List<String> backgroundColors = new ArrayList<>();
        backgroundColors.add("rgb(255, 99, 132)");
        backgroundColors.add("rgb(54, 162, 235)");
        backgroundColors.add("rgb(255, 205, 86)");
        backgroundColors.add("rgb(75, 192, 192)");
        backgroundColors.add("rgb(153, 102, 255)");
        dataSet.setBackgroundColor(backgroundColors);

        data.addChartDataSet(dataSet);

        List<String> labels = new ArrayList<>();
        // Make sure the number of labels matches the number of data points
        labels.add("18-25");
        labels.add("25-32");
        labels.add("32-40");
        labels.add("40-50");
        labels.add("50+");
        data.setLabels(labels);

        BarChartOptions options = new BarChartOptions();
        Legend legend = new Legend();
        legend.setDisplay(true); // Display the legend
        options.setLegend(legend);
        ageDistributionModel.setOptions(options);

        ageDistributionModel.setData(data);
    }

    public DonutChartModel getDonutModel() {
        return donutModel;
    }

    public void setDonutModel(DonutChartModel donutModel) {
        this.donutModel = donutModel;
    }

    public BarChartModel getAgeDistributionModel() {
        return ageDistributionModel;
    }

    public void setAgeDistributionModel(BarChartModel ageDistributionModel) {
        this.ageDistributionModel = ageDistributionModel;
    }

    


}
