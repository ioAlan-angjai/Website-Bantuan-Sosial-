// File: TreeNode.java

package com.donasi.donasiapi;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {

    private String name;
    private String type;
    private String desc;
    private double amount; 

    private List<TreeNode> children = new ArrayList<>();

    // Konstruktor default (Pengganti @NoArgsConstructor)
    public TreeNode() {
    }

    public TreeNode(String name, String type, String desc, double amount) {
        this.name = name;
        this.type = type;
        this.desc = desc;
        this.amount = amount;
    }

    public void addChild(TreeNode child) {
        this.children.add(child);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }



    public double getTotalAmount() {
        double total = this.amount; // Mulai dari uang diri sendiri

        // Jika punya anak, minta uang dari anak-anaknya juga
        if (children != null && !children.isEmpty()) {
            for (TreeNode child : children) {
                total += child.getTotalAmount(); // Panggil fungsi ini lagi di anak (Rekursif)
            }
        }

        return total;
    }
}