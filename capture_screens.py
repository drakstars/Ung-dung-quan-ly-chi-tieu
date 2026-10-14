import os
import subprocess
import time
import re
import xml.etree.ElementTree as ET

ADB = os.path.expandvars(r"$LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe")
SCREENSHOT_DIR = r"d:\Ứng dụng Quản lý chi tiêu cá nhân\screenshots"
os.makedirs(SCREENSHOT_DIR, exist_ok=True)

def run_adb(cmd):
    full_cmd = f'"{ADB}" {cmd}'
    res = subprocess.run(full_cmd, shell=True, capture_output=True, text=True, encoding="utf-8", errors="ignore")
    return res.stdout.strip()

def screenshot(filename):
    device_path = f"/sdcard/{filename}"
    local_path = os.path.join(SCREENSHOT_DIR, filename)
    run_adb(f"shell screencap -p {device_path}")
    run_adb(f"pull {device_path} \"{local_path}\"")
    run_adb(f"shell rm {device_path}")
    print(f"Captured: {filename}")
    time.sleep(1)

def get_node_center(resource_id=None, text=None):
    run_adb("shell uiautomator dump /sdcard/window_dump.xml")
    xml_str = run_adb("shell cat /sdcard/window_dump.xml")
    run_adb("shell rm /sdcard/window_dump.xml")
    try:
        root = ET.fromstring(xml_str)
        for node in root.iter("node"):
            r_id = node.attrib.get("resource-id", "")
            t = node.attrib.get("text", "")
            match_id = (resource_id in r_id) if resource_id else True
            match_text = (text in t) if text else True
            if match_id and match_text:
                bounds = node.attrib.get("bounds", "")
                m = re.findall(r"\[(\d+),(\d+)\]", bounds)
                if len(m) == 2:
                    x1, y1 = int(m[0][0]), int(m[0][1])
                    x2, y2 = int(m[1][0]), int(m[1][1])
                    return (x1 + x2) // 2, (y1 + y2) // 2
    except Exception as e:
        print(f"Error parsing xml: {e}")
    return None

def click_element(resource_id=None, text=None):
    center = get_node_center(resource_id, text)
    if center:
        print(f"Clicking on {resource_id or text} at {center}")
        run_adb(f"shell input tap {center[0]} {center[1]}")
        time.sleep(1)
        return True
    else:
        print(f"Could not find element: {resource_id or text}")
        return False

def main():
    print("Start capturing screenshots...")
    
    # 1. Mo LoginActivity
    run_adb("shell am force-stop com.example.quanlychitieu")
    time.sleep(1)
    run_adb("shell am start -n com.example.quanlychitieu/.LoginActivity")
    time.sleep(2)
    screenshot("01_login_initial.png")
    
    # 2. Click nut Dang nhap khi chua nhap gi -> Hien thong bao loi
    click_element(resource_id="btnLogin")
    time.sleep(1)
    screenshot("02_login_error.png")
    
    # 3. Nhap tai khoan dung
    click_element(resource_id="edtUsername")
    run_adb("shell input text admin")
    time.sleep(0.5)
    
    click_element(resource_id="edtPassword")
    run_adb("shell input text 123456")
    time.sleep(0.5)
    
    # An ban phim ao
    run_adb("shell input keyevent 111")
    time.sleep(1)
    screenshot("03_login_filled.png")
    
    # Click nut Dang nhap
    click_element(resource_id="btnLogin")
    time.sleep(2)
    
    # 4. Man hinh MainActivity ban dau
    screenshot("04_main_initial.png")
    
    # 5. Click nut "+ Them khoan chi"
    click_element(resource_id="btnAddExpense")
    time.sleep(2)
    screenshot("05_add_expense_empty.png")
    
    # Nhap ten khoan chi
    click_element(resource_id="edtExpenseName")
    run_adb("shell input text 'Ca%sphe'")
    time.sleep(0.5)
    
    # Nhap so tien
    click_element(resource_id="edtExpenseAmount")
    run_adb("shell input text 25000")
    time.sleep(0.5)
    
    # Cuon xuong de nhap ghi chu va xem nut Luu
    run_adb("shell input swipe 540 1800 540 600 300")
    time.sleep(1)
    
    click_element(resource_id="edtExpenseNote")
    run_adb("shell input text 'Uong%scafe%ssang'")
    time.sleep(0.5)
    
    run_adb("shell input keyevent 111")
    time.sleep(1)
    screenshot("06_add_expense_filled.png")
    
    # Click nut "Luu khoan chi"
    click_element(resource_id="btnSaveExpense")
    time.sleep(2)
    screenshot("07_main_after_add.png")
    
    # 6. Click vao khoan chi "Mua sach" de xem chi tiet
    click_element(text="Mua sách")
    time.sleep(2)
    screenshot("08_expense_detail.png")
    
    # 7. Click nut "Xoa khoan chi"
    click_element(resource_id="btnDeleteExpense")
    time.sleep(1)
    screenshot("09_delete_dialog.png")
    
    # Click nut "Xoa" tren AlertDialog
    run_adb("shell input tap 880 1310")
    # Hoac click bang text
    click_element(text="XÓA") or click_element(text="Xóa")
    time.sleep(2)
    screenshot("10_main_after_delete.png")
    
    print("Done capturing all screenshots!")

if __name__ == "__main__":
    main()
