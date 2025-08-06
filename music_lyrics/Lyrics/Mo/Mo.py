from time import sleep

BLUE = '\033[94m'
RED_BOLD = '\033[1;91m'
RESET = '\033[0m'

def print_lyrics():
    # Danh sách lời bài hát cho nam và nữ riêng biệt
    nam_lyrics = [
        ("Một năm vẫn có bốn mùa summer", 2.1),
        ("Người con gái cho anh cảm xúc", 3.3),
        ("Khi anh ngâm thơ", 2.8),
        ("Bao nhiêu anh tiếp cận", 3.3),
        ("Để xin number", 2.0),
        ("Có em ở cạnh bên", 2.5),
        ("Ngoài kia có lạnh thêm", 2.9),
        ("Một năm vẫn có bốn mùa summer", 3.0)
    ]

    nu_lyrics = [
        ("Loại bơ không thích", 2.1),
        ("Chính là anh bơ", 3.3),
        ("Rep tin nhắn nhanh", 3.3),
        ("Không để anh chờ", 2.8),
        ("Trò chơi đợi anh em là gamer", 3.2),
        ("Bắt em đợi lâu", 2.5),
        ("Em bắn anh giờ", 3.4),
        ("Bài thơ em viết", 2.7),
        ("Vào một ngày mơ", 3.0),
        ("Ngày mơ có anh cùng ăn burger", 4.0),
        ("Không muốn có fame", 3.3),
        ("Muốn có anh cơ", 3.6),
        ("Theo anh về nhà trước 22 giờ", 4.2)
    ]
    
    # In lời nam trước
    for line, delay in nam_lyrics:
        for char in line:
            print(f"{RED_BOLD}{char}{RESET}", end='', flush=True)
            sleep(delay / len(line))
        print()

    # Chờ một chút trước khi nữ hát
    sleep(1)

    # In lời nữ sau
    for line, delay in nu_lyrics:
        for char in line:
            print(f"{BLUE}{char}{RESET}", end='', flush=True)
            sleep(delay / len(line))
        print()

if __name__ == "__main__":
    print_lyrics()
