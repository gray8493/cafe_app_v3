

### Bước 3: Cập nhật Client (Chương trình Python)


import cv2
import base64
import requests
import time
import json
import keyboard

SERVER_URL = "http://localhost:8080/api/customer-display/analyze-face"

def find_available_camera():
    """Tìm camera có sẵn từ index 0 đến 4"""
    for index in range(5):  # Thử từ 0 đến 4
        cap = cv2.VideoCapture(index)
        if cap.isOpened():
            cap.release()
            return index
    return None

def main():
    camera_index = find_available_camera()
    if camera_index is None:
        print("Không tìm thấy webcam nào khả dụng. Vui lòng kiểm tra kết nối camera.")
        print("Gợi ý: Đảm bảo camera được kết nối và không bị chương trình khác sử dụng.")
        return

    cap = cv2.VideoCapture(camera_index)
    if not cap.isOpened():
        print(f"Không thể mở camera với index {camera_index}")
        return

    print("Chương trình đang chạy... Nhấn 'c' để chụp và phân tích, 'q' để thoát.")

    try:
        while True:
            ret, frame = cap.read()
            if not ret:
                print("Không thể đọc frame từ camera.")
                break

            # Sử dụng keyboard library thay vì cv2.waitKey để tránh lỗi GUI
            if keyboard.is_pressed('q'):
                print("Đang thoát chương trình...")
                break
            elif keyboard.is_pressed('c'):
                print("Đã chụp! Đang gửi ảnh để phân tích...")

                # Chuyển ảnh thành base64
                _, buffer = cv2.imencode('.jpg', frame)
                image_base64 = base64.b64encode(buffer).decode('utf-8')

                # Tạo payload
                payload = {"imageBase64": image_base64}

                try:
                    # Gửi đến server Spring Boot
                    response = requests.post(SERVER_URL, json=payload)

                    # print(f"Response status: {response.status_code}")
                    # print(f"Response content: {response.text}")

                    if response.status_code == 200:
                        suggestion_data = response.json()
                        print("\n--- GỢI Ý DÀNH CHO BẠN ---")
                        print(f"  Tuổi ước tính: {suggestion_data.get('age')}")
                        print(f"  Tâm trạng: {suggestion_data.get('emotion')}")
                        print(f"  Gợi ý: {suggestion_data.get('suggestion')}")
                        print("--------------------------\n")

                        # Đây là nơi bạn sẽ gửi dữ liệu đến màn hình hicển thị
                        # Ví dụ: send_to_display(suggestion_data)

                    else:
                        print(f"Lỗi từ server: {response.status_code} - {response.text}")

                except requests.exceptions.RequestException as e:
                    print(f"Không thể kết nối đến server: {e}")
                except Exception as e:
                    print(f"Lỗi JSON từ server: {e}")
                    if "Expecting value" in str(e):
                        print("Server trả về response không phải JSON hợp lệ. Hãy kiểm tra API key Face++.")

                # Chờ một chút để tránh chụp liên tục
                time.sleep(1)

            # Chờ một chút để giảm tải CPU
            time.sleep(0.1)
    except KeyboardInterrupt:
        print("\nĐã nhận tín hiệu dừng từ người dùng (Ctrl+C). Đang thoát...")
    finally:
        cap.release()

if __name__ == "__main__":
    main()