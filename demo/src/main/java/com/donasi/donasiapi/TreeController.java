package com.donasi.donasiapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*") 
public class TreeController {

    @Autowired
    private TreeService treeService;

    // 1. API UNTUK MENGAMBIL tree (GET)
    @GetMapping("/api/tree")
    public TreeNode getFullTree() {
        return treeService.getRootNode();
    }

    // 2. API BARU: Untuk Mengambil Total Donasi
    @GetMapping("/api/summary")
    public DonationSummary getDonationSummary() {
        return treeService.getSummary();
    }

    // 3. API UNTUK MENAMBAH node baru (POST)
 
    @PostMapping("/api/node/add")
    public TreeNode addNode(@RequestBody AddNodeRequest request) {
      
        treeService.recordDonation(request); 

    
        return treeService.getRootNode();
    }

    // 4. API UNTUK MENGHAPUS node (POST) (Tidak diubah)
    @PostMapping("/api/node/delete")
    public TreeNode deleteNode(@RequestBody DeleteNodeRequest request) {
        // Suruh 'TreeService' untuk menghapus
        treeService.deleteNode(request.getName());

        // Kembalikan pohon yang sudah di-update
        return treeService.getRootNode();
    }

    // 5. API UNTUK MENG-UPDATE node (POST) (Tidak diubah)
    @PostMapping("/api/node/update")
    public TreeNode updateNode(@RequestBody UpdateNodeRequest request) {
        // Suruh 'TreeService' untuk meng-update (termasuk nominal baru)
        treeService.updateNode(
                request.getOriginalName(),
                request.getNewName(),
                request.getNewType(),
                request.getNewDesc(),
                request.getNewAmount()
        );

        // Kembalikan pohon yang sudah di-update
        return treeService.getRootNode();
    }
}