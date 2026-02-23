

// --- Global Elements ---
const treeContainer = document.getElementById("tree");
const infoText = document.getElementById("infoText");
const infoDetails = document.getElementById("info-details"); 
const addNodeForm = document.getElementById("addNodeForm"); 
const aggregationDisplay = document.getElementById("aggregation-display"); 
const totalRecursiveAmount = document.getElementById("totalRecursiveAmount"); 

// --- Input Form Admin ---
const formTitle = document.getElementById("formTitle"); 
const formModeInput = document.getElementById("formMode"); 
const originalNodeNameInput = document.getElementById("originalNodeName");
const parentNameInput = document.getElementById("parentName");
const nodeNameInput = document.getElementById('nodeName');
const nodeTypeInput = document.getElementById('nodeType');
const nodeDescInput = document.getElementById('nodeDesc');
const nodeAmountInput = document.getElementById('nodeAmount'); 

// --- Global Elements Donasi Cepat ---
const donasiFormContainer = document.getElementById("donasiFormContainer");
const donasiForm = document.getElementById("donasiForm");
const btnJadiDonatur = document.getElementById("btnJadiDonatur");

// Input Donasi Cepat
const donasiNameInput = document.getElementById('donasiName');
const donasiTypeSelect = document.getElementById('donasiType');
const donasiAmountInput = document.getElementById('donasiAmount');
const donasiDescInput = document.getElementById('donasiDesc');
const targetUangFields = document.getElementById('targetUangFields');
const targetMakananFields = document.getElementById('targetMakananFields');
const targetBankSelect = document.getElementById('targetBank');
const targetMakananSelect = document.getElementById('targetMakanan');
const amountLabel = document.getElementById('amountLabel');
const cancelDonasiButton = document.getElementById('cancelDonasiButton');

// URL Backend
const API_URL = "http://localhost:8080/api";

// --- Utility Functions ---

function formatRupiah(number) {
    if (number === 0 || isNaN(number)) return 'Rp 0';
    // Menggunakan toLocaleString untuk format IDR
    return 'Rp ' + number.toLocaleString('id-ID', { minimumFractionDigits: 0, maximumFractionDigits: 0 });
}

// --- Render Tree Logic (Diubah agar konsisten dengan CSS Horizontal) ---
function renderTree(node) {
    // 1. Buat Node LI untuk menampung label dan anak-anak
    const li = document.createElement("li");

    // 2. Buat Label Node (Div)
    const label = document.createElement("div");
    label.classList.add("tree-node-label"); 
    label.textContent = node.name;
    li.appendChild(label);

    // Tambahkan event listener ke label
    label.addEventListener("click", (e) => {
        e.stopPropagation(); 
        showInfo(node);
    });

    // 3. Jika memiliki anak, buat UL untuk anak-anak
    if (node.children && node.children.length > 0) {
        const childrenUl = document.createElement("ul");
        node.children.forEach(child => {
            // Panggil rekursif dan tambahkan LI anak langsung ke UL anak
            childrenUl.appendChild(renderTree(child));
        });
        li.appendChild(childrenUl);
    }
    
    // 4. Kembalikan LI
    return li;
}


function showInfo(node) {
    infoText.textContent = `Detail: ${node.name}`;
    infoDetails.innerHTML = `
        <p><strong>Nama:</strong> ${node.name}</p>
        <p><strong>Tipe:</strong> ${node.type}</p>
        <p><strong>Keterangan:</strong> ${node.desc}</p>
        <p><strong>Nominal Sendiri:</strong> ${formatRupiah(node.amount)}</p>
        <p><strong>Anak Node:</strong> ${node.children.length}</p>
        <hr>
        <p><strong>Aksi Admin:</strong></p>
        <div class="form-buttons">
            <button class="btn btn-primary" onclick="showAddForm('${node.name}')">Tambah Anak</button>
            <button class="btn btn-warning" onclick="showEditForm('${node.name}', '${node.type}', '${node.desc}', ${node.amount})">Edit Node</button>
            ${node.name !== 'DanaSosial.org' ? `<button class="btn btn-danger" onclick="deleteNode('${node.name}')">Hapus Node</button>` : ''}
        </div>
    `;

    // Menggunakan node.totalAmount yang didapatkan dari method getTotalAmount() di Java
    if (node.totalAmount !== undefined && node.totalAmount !== 0) {
        totalRecursiveAmount.textContent = `TOTAL DONASI DITERIMA (Rekursif): ${formatRupiah(node.totalAmount)}`;
        totalRecursiveAmount.style.color = '#c62828';
    } else {
         totalRecursiveAmount.textContent = '';
    }
}

// --- Render Total Donasi ---

async function loadDonationSummary() {
    try {
        const response = await fetch(`${API_URL}/summary`);
        if (!response.ok) {
            throw new Error(`Gagal memuat ringkasan. Status: ${response.status}`);
        }
        const summary = await response.json();
        
        displaySummary(summary);
    } catch (error) {
        console.error("Error memuat ringkasan donasi:", error);
        aggregationDisplay.innerHTML = `<p style="color: red;">Gagal memuat total donasi.</p>`;
    }
}

function displaySummary(summary) {
    aggregationDisplay.innerHTML = `
        <div class="summary-card">
            <h4>Total Uang Terkumpul</h4>
            <div class="summary-value">${formatRupiah(summary.totalUangTerkumpul)}</div>
        </div>
        <div class="summary-card">
            <h4>Total Barang (Pcs/Kg)</h4>
            <div class="summary-value">${summary.totalPcsMakanan} Pcs/Kg</div>
        </div>
        <div class="summary-card">
            <h4>Jumlah Donatur</h4>
            <div class="summary-value">${summary.jumlahDonatur} Orang</div>
        </div>
    `;
}

// --- Main Tree  ---

async function loadTreeData() {
  const treeContainer = document.getElementById("tree");
  try {
    const response = await fetch(`${API_URL}/tree`);
    if (!response.ok) {
      throw new Error(`Koneksi ke backend gagal! Status: ${response.status}`);
    }

    const rootNode = await response.json();
    
    // 1. GAMBAR POHON
    treeContainer.innerHTML = ""; 
    
    // Buat UL terluar untuk menampung root LI
    const rootUl = document.createElement("ul");
    // Panggil renderTree untuk root, yang akan mengembalikan LI
    rootUl.appendChild(renderTree(rootNode)); 
    treeContainer.appendChild(rootUl); 
    
    // 2. Tampilkan info root node saat pertama kali load
    showInfo(rootNode);

    // 3. MUAT TOTAL DONASI
    await loadDonationSummary();

  } catch (error) {
    console.error("Error memuat data tree:", error);
    treeContainer.innerHTML = `<p style="color: red;">Gagal terhubung ke backend Java. Pastikan server Java (Spring Boot) sudah di-RUN di port 8080.</p>`;
    aggregationDisplay.innerHTML = `<p style="color: red;">Gagal terhubung ke backend.</p>`;
  }
}

// --- Form Admin Handling (Tambah, Edit, Hapus) ---
function showAddForm(parentNodeName) {
    formTitle.textContent = `Tambah Anak Node di bawah ${parentNodeName}`;
    formModeInput.value = 'add';
    originalNodeNameInput.value = '';
    parentNameInput.value = parentNodeName;
    nodeNameInput.value = '';
    nodeTypeInput.value = '';
    nodeDescInput.value = '';
    nodeAmountInput.value = 0;
    addNodeForm.style.display = 'block';
}

function showEditForm(name, type, desc, amount) {
    formTitle.textContent = `Edit Node: ${name}`;
    formModeInput.value = 'edit';
    originalNodeNameInput.value = name;
    parentNameInput.value = ''; 
    nodeNameInput.value = name;
    nodeTypeInput.value = type;
    nodeDescInput.value = desc;
    nodeAmountInput.value = amount;
    addNodeForm.style.display = 'block';
}

async function handleFormSubmit(event) {
    event.preventDefault();
    
    const mode = formModeInput.value;
    const originalName = originalNodeNameInput.value;
    const parentName = parentNameInput.value;
    const name = nodeNameInput.value;
    const type = nodeTypeInput.value;
    const desc = nodeDescInput.value;
    const amount = parseFloat(nodeAmountInput.value || 0); 

    let endpoint = '';
    let body = {};

    if (mode === 'add') {
        endpoint = '/node/add';
        // Note: Untuk form admin, type-nya diisi manual, bukan selalu 'Donatur'
        body = { parentName, name, type, desc, amount }; 
    } else if (mode === 'edit') {
        endpoint = '/node/update';
        body = { originalName, newName: name, newType: type, newDesc: desc, newAmount: amount };
    }

    try {
        const response = await fetch(`${API_URL}${endpoint}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });

        if (!response.ok) {
            throw new Error('Gagal menyimpan node. Cek konsol backend.');
        }

        addNodeForm.style.display = 'none';
        await loadTreeData();
    } catch (error) {
        console.error("Error form submit:", error);
        alert('Gagal menyimpan data: ' + error.message);
    }
}

async function deleteNode(name) {
    if (!confirm(`Yakin ingin menghapus node "${name}"? Aksi ini permanen.`)) return;

    try {
        const response = await fetch(`${API_URL}/node/delete`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name })
        });

        if (!response.ok) {
            throw new Error('Gagal menghapus node. Node yang memiliki anak atau root tidak bisa dihapus.');
        }

        await loadTreeData();
    } catch (error) {
        console.error("Error delete:", error);
        alert('Gagal menghapus data: ' + error.message);
    }
}


// --- Form Donasi Cepat Logic ---

function showDonasiForm() {
    donasiFormContainer.style.display = 'flex'; 
}

function handleDonasiTypeChange() {
    const type = donasiTypeSelect.value;
    
    targetUangFields.style.display = 'none';
    targetMakananFields.style.display = 'none';
    targetBankSelect.removeAttribute('required');
    targetMakananSelect.removeAttribute('required');

    if (type === 'Donasi Uang') {
        targetUangFields.style.display = 'block';
        amountLabel.textContent = 'Nominal Donasi (Rp):';
        targetBankSelect.setAttribute('required', 'required');
        donasiAmountInput.placeholder = 'Cth: 50000';
    } else if (type === 'Donasi Makanan') {
        targetMakananFields.style.display = 'block';
        amountLabel.textContent = 'Jumlah Barang (Pcs/Kg):';
        targetMakananSelect.setAttribute('required', 'required');
        donasiAmountInput.placeholder = 'Cth: 50 (untuk 50 kg/pcs)';
    }
}

async function handleDonasiSubmit(event) {
    event.preventDefault();

    const name = donasiNameInput.value;
    const type = donasiTypeSelect.value;
    const amount = parseFloat(donasiAmountInput.value);
    const desc = donasiDescInput.value;

    let parentName = '';
    
    if (type === 'Donasi Uang') {
        parentName = targetBankSelect.value;
    } else if (type === 'Donasi Makanan') {
        parentName = targetMakananSelect.value;
    }

    if (!parentName) {
        alert("Silakan pilih target donasi (Bank/Jenis Makanan).");
        return;
    }
    
   
    const body = { parentName, name, type: 'Donatur', desc, amount }; 

    try {
        const response = await fetch(`${API_URL}/node/add`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });

        if (!response.ok) {
            throw new Error('Gagal mencatat donasi. Cek konsol backend.');
        }

        alert(`Terima kasih, Donasi ${type} dari ${name} sebesar ${formatRupiah(amount)} berhasil dicatat!`);
        
        donasiFormContainer.style.display = 'none';
        donasiForm.reset();
        handleDonasiTypeChange(); 
        await loadTreeData();
    } catch (error) {
        console.error("Error donasi submit:", error);
        alert('Gagal mencatat donasi: ' + error.message);
    }
}


// --- Event Listeners ---
document.addEventListener('DOMContentLoaded', loadTreeData);
addNodeForm.addEventListener('submit', handleFormSubmit);

document.getElementById('cancelButton').addEventListener('click', (e) => {
  e.preventDefault(); 
  addNodeForm.style.display = 'none';
});

// Event Listeners Donasi Cepat
btnJadiDonatur.addEventListener('click', showDonasiForm);
cancelDonasiButton.addEventListener('click', (e) => {
    e.preventDefault();
    donasiFormContainer.style.display = 'none';
    donasiForm.reset();
    handleDonasiTypeChange(); 
});

donasiTypeSelect.addEventListener('change', handleDonasiTypeChange);
donasiForm.addEventListener('submit', handleDonasiSubmit);


handleDonasiTypeChange();