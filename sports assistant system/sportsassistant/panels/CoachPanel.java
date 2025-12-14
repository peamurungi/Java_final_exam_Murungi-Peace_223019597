package com.sportsassistant.panels;

import com.sportsassistant.dao.CoachDAO;
import com.sportsassistant.model.Coach;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CoachPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JButton addBtn, editBtn, deleteBtn, refreshBtn;
    private CoachDAO dao = new CoachDAO();

    public CoachPanel() {
        setLayout(new BorderLayout());
        model = new DefaultTableModel(new String[]{"ID","Name","Experience","Team ID"},0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        addBtn = new JButton("Add");
        editBtn = new JButton("Edit");
        deleteBtn = new JButton("Delete");
        refreshBtn = new JButton("Refresh");
        panel.add(addBtn); panel.add(editBtn); panel.add(deleteBtn); panel.add(refreshBtn);
        add(panel, BorderLayout.SOUTH);

        loadData();

        refreshBtn.addActionListener(e -> loadData());

        addBtn.addActionListener(e -> {
            JTextField name = new JTextField();
            JTextField exp = new JTextField();
            JTextField teamId = new JTextField();
            Object[] msg = {"Name:", name, "Experience:", exp, "Team ID:", teamId};
            if(JOptionPane.showConfirmDialog(this,msg,"Add Coach",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
                try{
                    Coach c = new Coach();
                    c.setName(name.getText());
                    c.setExperience(Integer.parseInt(exp.getText()));
                    c.setTeamId(Integer.parseInt(teamId.getText()));
                    dao.addCoach(c);
                    loadData();
                }catch(Exception ex){ex.printStackTrace();}
            }
        });

        editBtn.addActionListener(e -> {
            int sel = table.getSelectedRow();
            if(sel>=0){
                int id = (int)model.getValueAt(sel,0);
                JTextField name = new JTextField((String)model.getValueAt(sel,1));
                JTextField exp = new JTextField(model.getValueAt(sel,2).toString());
                JTextField teamId = new JTextField(model.getValueAt(sel,3).toString());
                Object[] msg = {"Name:", name, "Experience:", exp, "Team ID:", teamId};
                if(JOptionPane.showConfirmDialog(this,msg,"Edit Coach",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
                    try{
                        Coach c = new Coach(id,name.getText(),Integer.parseInt(exp.getText()),Integer.parseInt(teamId.getText()));
                        dao.updateCoach(c);
                        loadData();
                    }catch(Exception ex){ex.printStackTrace();}
                }
            }
        });

        deleteBtn.addActionListener(e -> {
            int sel = table.getSelectedRow();
            if(sel>=0){
                int id = (int)model.getValueAt(sel,0);
                if(JOptionPane.showConfirmDialog(this,"Delete selected coach?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                    try{dao.deleteCoach(id);loadData();}catch(Exception ex){ex.printStackTrace();}
                }
            }
        });
    }

    private void loadData(){
        try{
            List<Coach> list = dao.getAllCoaches();
            model.setRowCount(0);
            for(Coach c:list) model.addRow(new Object[]{c.getCoachId(),c.getName(),c.getExperience(),c.getTeamId()});
        }catch(Exception ex){ex.printStackTrace();}
    }
}
