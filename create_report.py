import os
import docx
from docx import Document
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT, WD_ALIGN_VERTICAL
from docx.oxml import parse_xml, OxmlElement
from docx.oxml.ns import nsdecls, qn

def create_report():
    doc = Document()
    
    # Canh lề chuẩn A4
    for section in doc.sections:
        section.top_margin = Inches(0.8)
        section.bottom_margin = Inches(0.8)
        section.left_margin = Inches(0.8)
        section.right_margin = Inches(0.8)

    # Tiêu đề báo cáo
    title = doc.add_paragraph()
    title.alignment = WD_ALIGN_PARAGRAPH.CENTER
    run_title = title.add_run("BÁO CÁO BÀI TẬP LẬP TRÌNH ANDROID\nỨNG DỤNG QUẢN LÝ CHI TIÊU CÁ NHÂN")
    run_title.font.name = "Arial"
    run_title.font.size = Pt(18)
    run_title.font.bold = True
    run_title.font.color.rgb = RGBColor(0, 105, 92) # Teal

    p_sub = doc.add_paragraph()
    p_sub.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r_sub = p_sub.add_run("Báo cáo Hình ảnh - Mô tả chi tiết 4 màn hình chức năng\n")
    r_sub.font.name = "Arial"
    r_sub.font.size = Pt(12)
    r_sub.font.italic = True
    r_sub.font.color.rgb = RGBColor(100, 116, 139)

    screens_data = [
        {
            "name": "1. Màn hình Đăng nhập (LoginActivity)",
            "img": r"d:\Ứng dụng Quản lý chi tiêu cá nhân\screenshots\02_login_wrong_password.png",
            "img_caption": "Hình 1: Màn hình LoginActivity khi kiểm tra báo lỗi sai thông tin hoặc bỏ trống",
            "desc": [
                "• Thành phần giao diện đã thêm:",
                "  + Logo tròn màu xanh với biểu tượng đồng tiền Việt Nam (đ) ở giữa.",
                "  + EditText edtUsername: nhập tên đăng nhập (hint: 'Nhập tên đăng nhập').",
                "  + EditText edtPassword: nhập mật khẩu với android:inputType='textPassword' để ẩn ký tự.",
                "  + TextView tvErrorMessage: hiển thị dòng lỗi màu đỏ 'Sai tên đăng nhập hoặc mật khẩu' khi nhập sai hoặc 'Vui lòng nhập đầy đủ thông tin' khi bỏ trống.",
                "  + Button btnLogin: nút 'Đăng nhập' màu xanh chủ đạo bo tròn các góc.",
                "  + TextView tvSampleAccount: hiển thị gợi ý 'Tài khoản mẫu: admin / 123456', có tích hợp tính năng chạm vào để tự động điền nhanh tài khoản.",
                "• Tài khoản cố định: Khai báo hằng số trong code LoginActivity.java:",
                "  private static final String VALID_USERNAME = \"admin\";",
                "  private static final String VALID_PASSWORD = \"123456\";",
                "• Kiểm tra bỏ trống / sai thông tin: Dùng hàm edtUsername.getText().toString().trim().isEmpty() để kiểm tra rỗng; so sánh chuỗi bằng equals(). Khi sai thì setText cho tvErrorMessage và hiện Toast thông báo.",
                "• Chuyển sang MainActivity bằng Intent: Khi đăng nhập đúng, tạo Intent intent = new Intent(LoginActivity.this, MainActivity.class); gọi startActivity(intent) và gọi finish() để người dùng không quay lại màn hình đăng nhập bằng nút Back."
            ]
        },
        {
            "name": "2. Màn hình Danh sách chi tiêu (MainActivity)",
            "img": r"d:\Ứng dụng Quản lý chi tiêu cá nhân\screenshots\04_main_initial.png",
            "img_caption": "Hình 2: Màn hình MainActivity hiển thị tổng tiền đã chi và 3 khoản chi mẫu ban đầu",
            "desc": [
                "• Thành phần giao diện đã thêm:",
                "  + Top Bar tiêu đề ứng dụng 'Quản lý chi tiêu'.",
                "  + Card nền xanh bo góc hiển thị 'Tổng số tiền đã chi' và số tiền 200.000 đ (tvTotalAmount) nổi bật.",
                "  + RecyclerView rvExpenses hiển thị danh sách các khoản chi (sử dụng Adapter tùy biến ExpenseAdapter).",
                "  + Mỗi item (item_expense.xml) hiển thị: Tên khoản chi (in đậm), Số tiền (màu đỏ có dấu chấm phân cách hàng nghìn), Danh mục (badge bo tròn có màu sắc riêng biệt cho từng loại) và Ngày chi tiêu.",
                "  + Dòng hướng dẫn: '(nhấn vào một khoản chi để xem chi tiết)'.",
                "  + Nút Button btnAddExpense '+ Thêm khoản chi' ở dưới cùng.",
                "• Dữ liệu mẫu ban đầu: Khởi tạo trong hàm initSampleData() gồm 3 khoản chi: Ăn sáng (30.000 đ - Ăn uống), Đổ xăng (50.000 đ - Đi lại), Mua sách (120.000 đ - Học tập).",
                "• Cách tính và cập nhật tổng tiền: Hàm updateTotalAmount() duyệt qua toàn bộ ArrayList<Expense>, cộng dồn amount và format qua hàm Expense.formatMoney() rồi gán vào tvTotalAmount.",
                "• Xử lý sự kiện:",
                "  + Nhấn nút '+ Thêm khoản chi': Sử dụng ActivityResultLauncher mở AddExpenseActivity, nhận kết quả trả về để thêm đối tượng Expense mới vào danh sách và cập nhật tổng tiền.",
                "  + Nhấn vào một khoản chi: ExpenseAdapter lắng nghe onItemClick, truyền đối tượng Expense và vị trí (position) qua Intent sang ExpenseDetailActivity."
            ]
        },
        {
            "name": "3. Màn hình Thêm khoản chi (AddExpenseActivity)",
            "img": r"d:\Ứng dụng Quản lý chi tiêu cá nhân\screenshots\06_add_expense_filled.png",
            "img_caption": "Hình 3: Màn hình AddExpenseActivity khi nhập liệu đầy đủ các trường thông tin",
            "desc": [
                "• Thành phần giao diện đã thêm:",
                "  + Top Bar có nút mũi tên quay lại (btnBackAdd) và tiêu đề 'Thêm khoản chi'.",
                "  + EditText edtExpenseName: nhập tên khoản chi (hint: 'Ví dụ: Ăn trưa').",
                "  + EditText edtExpenseAmount: nhập số tiền với android:inputType='number'.",
                "  + Spinner spnCategory: danh sách danh mục gồm 5 mục: 'Ăn uống', 'Đi lại', 'Học tập', 'Giải trí', 'Khác'.",
                "  + DatePicker datePicker: giao diện con lăn (spinner mode) chọn ngày, tháng, năm trực quan.",
                "  + EditText edtExpenseNote: nhập ghi chú nhiều dòng với android:inputType='textMultiLine' và minLines='3'.",
                "  + Button btnSaveExpense: nút 'Lưu khoản chi'.",
                "• Kiểm tra dữ liệu hợp lệ: Kiểm tra tên khoản chi không được để trống (báo lỗi qua edtExpenseName.setError); kiểm tra số tiền phải hợp lệ và lớn hơn 0 (Long.parseLong > 0).",
                "• Tạo đối tượng Expense và trả về kết quả: Lấy ngày từ DatePicker định dạng dd/MM/yyyy; tạo new Expense(id, name, amount, category, date, note); đóng gói vào Intent intent.putExtra(MainActivity.EXTRA_EXPENSE, newExpense) và gọi setResult(Activity.RESULT_OK, intent), sau đó finish() quay về danh sách."
            ]
        },
        {
            "name": "4. Màn hình Chi tiết khoản chi (ExpenseDetailActivity)",
            "img": r"d:\Ứng dụng Quản lý chi tiêu cá nhân\screenshots\08_expense_detail.png",
            "img_caption": "Hình 4: Màn hình ExpenseDetailActivity hiển thị chi tiết khoản chi 'Mua sách'",
            "desc": [
                "• Thành phần giao diện đã thêm:",
                "  + Top Bar có nút mũi tên quay lại và tiêu đề 'Chi tiết khoản chi'.",
                "  + Card thông tin nền trắng bo góc hiển thị: Tên khoản chi (chữ to đậm), Số tiền (màu đỏ đậm phân cách hàng nghìn '120.000 đ'), đường kẻ ngang phân cách.",
                "  + Các trường thông tin chi tiết: Danh mục ('Học tập'), Ngày chi tiêu ('13/10/2026'), Ghi chú ('Sách Lập trình Android').",
                "  + Nút 'Quay lại' (btnBack): kiểu viền xanh (outline), đóng Activity và trở về màn hình trước.",
                "  + Nút 'Xóa khoản chi' (btnDeleteExpense): màu đỏ nổi bật.",
                "• Cách nhận dữ liệu qua Intent: Dùng getIntent().getSerializableExtra(MainActivity.EXTRA_EXPENSE) để lấy đối tượng Expense và getIntExtra(MainActivity.EXTRA_POSITION, -1) để lấy vị trí item trong danh sách.",
                "• Xử lý nút Quay lại và Xóa:",
                "  + Nhấn 'Quay lại': gọi finish() giữ nguyên dữ liệu.",
                "  + Nhấn 'Xóa khoản chi': hiển thị AlertDialog xác nhận 'Bạn có chắc chắn muốn xóa khoản chi này không?'. Khi xác nhận, gửi Intent mang ACTION_DELETE và position về cho MainActivity bằng setResult(Activity.RESULT_OK, resultIntent) và finish().",
                "  + MainActivity nhận tín hiệu xóa, loại bỏ item khỏi ArrayList, thông báo adapter.notifyItemRemoved và tự động cập nhật lại tổng số tiền đã chi."
            ]
        }
    ]

    for item in screens_data:
        h2 = doc.add_heading(item["name"], level=2)
        h2.paragraph_format.space_before = Pt(14)
        h2.paragraph_format.space_after = Pt(6)
        for run in h2.runs:
            run.font.name = "Arial"
            run.font.color.rgb = RGBColor(0, 105, 92)

        # Chèn ảnh nếu file tồn tại
        if os.path.exists(item["img"]):
            p_img = doc.add_paragraph()
            p_img.alignment = WD_ALIGN_PARAGRAPH.CENTER
            p_img.paragraph_format.space_before = Pt(6)
            p_img.paragraph_format.space_after = Pt(4)
            run_img = p_img.add_run()
            run_img.add_picture(item["img"], width=Inches(2.6))
            
            p_cap = doc.add_paragraph()
            p_cap.alignment = WD_ALIGN_PARAGRAPH.CENTER
            p_cap.paragraph_format.space_after = Pt(8)
            r_cap = p_cap.add_run(item["img_caption"])
            r_cap.font.name = "Arial"
            r_cap.font.size = Pt(9.5)
            r_cap.font.italic = True
            r_cap.font.color.rgb = RGBColor(100, 116, 139)

        # Mô tả chi tiết
        for line in item["desc"]:
            p_desc = doc.add_paragraph()
            p_desc.paragraph_format.space_after = Pt(3)
            p_desc.paragraph_format.line_spacing = 1.15
            r_line = p_desc.add_run(line)
            r_line.font.name = "Arial"
            r_line.font.size = Pt(10.5)
            if line.startswith("•"):
                r_line.font.bold = True
                r_line.font.color.rgb = RGBColor(30, 41, 59)
            elif line.startswith("  +"):
                r_line.font.color.rgb = RGBColor(51, 65, 85)
            else:
                r_line.font.color.rgb = RGBColor(71, 85, 105)

    # Thêm bảng tổng hợp kiểm thử chức năng
    h_test = doc.add_heading("5. Bảng tổng hợp kiểm thử chức năng ứng dụng", level=2)
    h_test.paragraph_format.space_before = Pt(16)
    for run in h_test.runs:
        run.font.name = "Arial"
        run.font.color.rgb = RGBColor(0, 105, 92)

    table = doc.add_table(rows=1, cols=4)
    table.alignment = WD_TABLE_ALIGNMENT.CENTER
    hdr_cells = table.rows[0].cells
    headers = ["Màn hình / Chức năng", "Dữ liệu nhập / Thao tác", "Kết quả mong đợi", "Đánh giá"]
    for i, h_text in enumerate(headers):
        hdr_cells[i].text = h_text
        set_cell_background(hdr_cells[i], "00695C")
        for p in hdr_cells[i].paragraphs:
            for r in p.runs:
                r.font.name = "Arial"
                r.font.bold = True
                r.font.size = Pt(10)
                r.font.color.rgb = RGBColor(255, 255, 255)

    test_cases = [
        ("Đăng nhập bỏ trống", "Bấm Đăng nhập khi để trống tên hoặc mật khẩu", "Hiển thị thông báo lỗi yêu cầu nhập đầy đủ", "Đạt"),
        ("Đăng nhập sai", "Nhập admin / sai_pass", "Hiện lỗi 'Sai tên đăng nhập hoặc mật khẩu'", "Đạt"),
        ("Đăng nhập đúng", "Nhập admin / 123456", "Chuyển sang MainActivity, finish() LoginActivity", "Đạt"),
        ("Danh sách & Tổng tiền", "Vào màn hình chính", "Hiển thị 3 khoản chi mẫu, tổng tiền 200.000 đ", "Đạt"),
        ("Validate thêm mới", "Để trống tên hoặc số tiền <= 0", "Báo lỗi setError tại ô nhập liệu", "Đạt"),
        ("Thêm khoản chi", "Nhập 'Cà phê sáng', 25.000 đ", "Thêm vào danh sách, tổng tiền tăng lên 225.000 đ", "Đạt"),
        ("Xem chi tiết", "Nhấn vào khoản chi 'Mua sách'", "Hiển thị đầy đủ tên, 120.000 đ, Học tập, ghi chú", "Đạt"),
        ("Xác nhận & Xóa", "Bấm nút Xóa khoản chi -> Chọn Xóa", "Khoản chi bị xóa, tổng tiền giảm chính xác", "Đạt")
    ]

    for row_data in test_cases:
        row_cells = table.add_row().cells
        for col_idx, cell_value in enumerate(row_data):
            row_cells[col_idx].text = cell_value
            for p in row_cells[col_idx].paragraphs:
                for r in p.runs:
                    r.font.name = "Arial"
                    r.font.size = Pt(9.5)
                    if col_idx == 3:
                        r.font.bold = True
                        r.font.color.rgb = RGBColor(0, 150, 136)

    output_path = r"d:\Ứng dụng Quản lý chi tiêu cá nhân\Bao_Cao_Ung_Dung_Quan_Ly_Chi_Tieu.docx"
    doc.save(output_path)
    print(f"Report created successfully at: {output_path}")

def set_cell_background(cell, fill_hex):
    tcPr = cell._tc.get_or_add_tcPr()
    shd = parse_xml(f'<w:shd {nsdecls("w")} w:fill="{fill_hex}"/>')
    tcPr.append(shd)

if __name__ == "__main__":
    create_report()
