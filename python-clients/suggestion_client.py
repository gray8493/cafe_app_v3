

### Bước 3: Cập nhật Client (Chương trình Python)


import cv2
import base64
import requests
import time
import json

SERVER_URL = "http://localhost:8080/api/customer-display/analyze-face"
CAMERA_INDEX = 0

def main():
    cap = cv2.VideoCapture(CAMERA_INDEX)
    if not cap.isOpened():
        print("Không thể mở webcam")
        return
        
    print("Chương trình đang chạy... Nhấn 'c' để chụp và phân tích, 'q' để thoát.")
    
    while True:
        ret, frame = cap.read()
        if not ret:
            break
        
        cv2.imshow('Dr. Drink - Press C to capture, Q to quit', frame)
        
        key = cv2.waitKey(1) & 0xFF
        
        if key == ord('q'):
            break
        elif key == ord('c'):
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

                    # Đây là nơi bạn sẽ gửi dữ liệu đến màn hình hiển thị
                    # Ví dụ: send_to_display(suggestion_data)

                else:
                    print(f"Lỗi từ server: {response.status_code} - {response.text}")

            except requests.exceptions.RequestException as e:
                print(f"Không thể kết nối đến server: {e}")
            except Exception as e:
                print(f"Lỗi JSON từ server: {e}")
                if "Expecting value" in str(e):
                    print("Server trả về response không phải JSON hợp lệ. Hãy kiểm tra API key Google AI.")

    cap.release()
    cv2.destroyAllWindows()

if __name__ == "__main__":
    main()