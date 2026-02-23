
package com.donasi.donasiapi;

import java.util.Iterator;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class TreeService {

    private TreeNode root;
    private DonationSummary summary = new DonationSummary(); 

    @PostConstruct
    public void initializeTree() {
        // --- LEVEL 0: ROOT ---
        root = new TreeNode("DanaSosial.org", "Penadah Donatur", "Pusat penyaluran donasi.", 0);

        // --- LEVEL 1: KATEGORI UTAMA ---
        TreeNode donasiUang = new TreeNode("Donasi Uang", "Kategori", "Bantuan dana tunai.", 0);
        TreeNode donasiMakanan = new TreeNode("Donasi Makanan", "Kategori", "Bantuan logistik pangan.", 0);

        root.addChild(donasiUang);
        root.addChild(donasiMakanan);


        // Level 2: Bank
        TreeNode bankBRI = new TreeNode("Bank BRI", "Bank", "Rekening BRI.", 0);
        TreeNode bankBNI = new TreeNode("Bank BNI", "Bank", "Rekening BNI.", 0);

        donasiUang.addChild(bankBRI);
        donasiUang.addChild(bankBNI);
        

        // Level 2: Jenis Makanan
        TreeNode bahanMentah = new TreeNode("Bahan Mentah", "Jenis", "Sembako & mentahan.", 0);
        TreeNode siapSaji = new TreeNode("Makanan Siap Saji", "Jenis", "Makanan matang.", 0);

        donasiMakanan.addChild(bahanMentah);
        donasiMakanan.addChild(siapSaji);
    }

    
    // Getter untuk Root Node
    public TreeNode getRootNode() {

        return this.root;
    }
    
    public DonationSummary getSummary() { return this.summary; } 

    public void recordDonation(AddNodeRequest request) {
        // 1. Buat node donatur baru
        TreeNode newNode = new TreeNode(
            request.getName(),
            "Donatur", // Tipe donatur selalu "Donatur"
            request.getDesc(),
            request.getAmount() // Nominal donasi
        );

        // 2. Tambahkan node ke parent (Bank/Jenis Makanan)
        addNodeToParent(request.getParentName(), newNode);

        // 3. Update summary global
        if (request.getParentName().contains("Bank")) {
             summary.setTotalUangTerkumpul(summary.getTotalUangTerkumpul() + request.getAmount());
        } else {
             summary.setTotalPcsMakanan(summary.getTotalPcsMakanan() + request.getAmount());
        }
        
        // 4. Tambahkan jumlah donatur
        summary.setJumlahDonatur(summary.getJumlahDonatur() + 1);
    }
    
    public TreeNode addNodeToParent(String parentName, TreeNode newNode) {
        TreeNode parent = findNodeByName(this.root, parentName);
        if (parent != null) {
            parent.addChild(newNode);
            return root;
        } else {
            throw new RuntimeException("Parent node not found: " + parentName);
        }
    }

    public void deleteNode(String name) {
        if (root.getName().equals(name)) throw new RuntimeException("Cannot delete root!");
        if (!findAndRemove(this.root, name)) {
            throw new RuntimeException("Node not found: " + name);
        }
    }

    public void updateNode(String originalName, String newName, String newType, String newDesc, double newAmount) {
        TreeNode nodeToUpdate = findNodeByName(this.root, originalName);
        if (nodeToUpdate != null) {
            
            if (nodeToUpdate.getType().equals("Donatur")) {
                double diff = newAmount - nodeToUpdate.getAmount();
                
                TreeNode parent = findParent(this.root, originalName);
                if (parent != null) {
                     if (parent.getName().contains("Bank")) {
                         summary.setTotalUangTerkumpul(summary.getTotalUangTerkumpul() + diff);
                     } else {
                         summary.setTotalPcsMakanan(summary.getTotalPcsMakanan() + diff);
                     }
                }
            }
            
            nodeToUpdate.setName(newName);
            nodeToUpdate.setType(newType);
            nodeToUpdate.setDesc(newDesc);
            nodeToUpdate.setAmount(newAmount); 
        } else {
            throw new RuntimeException("Node not found: " + originalName);
        }
    }

    private TreeNode findNodeByName(TreeNode node, String name) {
        if (node.getName().equals(name)) return node;
        for (TreeNode child : node.getChildren()) {
            TreeNode found = findNodeByName(child, name);
            if (found != null) return found;
        }
        return null;
    }
    
    private TreeNode findParent(TreeNode node, String childName) {
        for (TreeNode child : node.getChildren()) {
            if (child.getName().equals(childName)) return node;
            TreeNode parent = findParent(child, childName);
            if (parent != null) return parent;
        }
        return null;
    }

    private boolean findAndRemove(TreeNode parent, String nameToRemove) {
        Iterator<TreeNode> iterator = parent.getChildren().iterator();
        while (iterator.hasNext()) {
            TreeNode child = iterator.next();
            if (child.getName().equals(nameToRemove)) {
                
                if (child.getType().equals("Donatur")) {
                    summary.setJumlahDonatur(summary.getJumlahDonatur() - 1);
                    if (parent.getName().contains("Bank")) {
                        summary.setTotalUangTerkumpul(summary.getTotalUangTerkumpul() - child.getAmount());
                    } else {
                        summary.setTotalPcsMakanan(summary.getTotalPcsMakanan() - child.getAmount());
                    }
                }
                
                iterator.remove();
                return true;
            }
            if (findAndRemove(child, nameToRemove)) return true;
        }
        return false;
    }
}