from time import sleep

# ANSI color codes
COLORS = {"male": "\033[94m", "female": "\033[1;91m", "reset": "\033[0m"}


# Hiệu ứng gõ từng ký tự
def type_with_effect(text, color, speed):
    for char in text:
        print(f"{color}{char}{COLORS['reset']}", end="", flush=True)
        sleep(speed)
    print()  # Xuống dòng sau mỗi câu


# In lời bài hát
def print_lyrics():
    # Danh sách lời bài hát và thời gian cho từng câu
    nam_lyrics = [
        ("Một năm vẫn có bốn mùa summer", 2.5),
        ("Người con gái cho anh cảm xúc", 1.2),
        ("Khi anh ngâm thơ", 1.1),
        ("Bao nhiêu anh tiếp cận", 0.8),
        ("Để xin number", 1.1),
        ("Có em ở cạnh bên", 1.0),
        ("Ngoài kia có lạnh thêm", 0.9),
        ("Một năm vẫn có bốn mùa summer", 2.5),
    ]

    nu_lyrics = [
        ("Loại bơ không thích", 0.9),
        ("Chính là anh bơ", 1.1),
        ("Rep tin nhắn nhanh", 1.0),
        ("Không để anh chờ", 1.2),
        ("Trò chơi đợi anh em là gamer", 2.2),
        ("Bắt em đợi lâu", 1.1),
        ("Em bắn anh giờ", 1.1),
        ("Bài thơ em viết", 1.1),
        ("Vào một ngày mơ", 1.2),
        ("Ngày mơ có anh cùng ăn burger", 2.3),
        ("Không muốn có fame", 1.1),
        ("Muốn có anh cơ", 1.2),
        ("Theo anh về nhà trước 22 giờ", 3.3),
    ]

    for text, total_time in nam_lyrics:
        if text.strip():
            speed = total_time / len(text)
            type_with_effect(text, COLORS["male"], speed)

    for text, total_time in nu_lyrics:
        if text.strip():
            speed = total_time / len(text)
            type_with_effect(text, COLORS["female"], speed)


if __name__ == "__main__":
    print("\n       🎵 MƠ 🎵\n")
    print_lyrics()
    print("\n     ✨THE END✨\n")
