import utils

if __name__ == "__main__":
    while True:
        x=input("Number: ")
        if x=='q':
            break
        print(utils.process_item(int(x)))
    else:
        print("Exit")