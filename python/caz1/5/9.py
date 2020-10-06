from functools import partial
import os
import pathlib


def search_by_content(target, to_search: str) -> list:
    if os.path.isfile(target):
        return [target] if to_search in open(target).read() else []
    result = []
    for root, directory, files in os.walk(target):
        for file in files:
            if to_search in open(os.path.join(root, file), 'r').read():
                result.append(os.path.join(root, file))
    return result


def get_file_info(file_path: str) -> dict:
    return {
        'full_path': os.path.abspath(file_path),
        'file_size': os.path.getsize(file_path),
        'file_extension': pathlib.Path(file_path).suffix,
        'can_read//write': os.access(file_path, os.R_OK and os.W_OK)
    }


def ex3(file_path: str) -> None:
    with open(file_path, 'w') as file:
        file.writelines([f'{key}    {value}\n' for key,
                         value in os.environ.items()])


def ex4(path_dir: str):
    result = []
    dirs = []
    for i in os.listdir(path_dir):
        temp_path = os.path.join(path_dir, i)
        if os.path.isdir(temp_path):
            result.append((temp_path, 'dir'))
            dirs.append(temp_path)
        elif os.path.isfile(temp_path):
            result.append((temp_path, 'file'))
        else:
            result.append((temp_path, 'unknow'))
    for i in dirs:
        result += ex4(i)
    return result


def ex5(source: str, directory: str, buffer_size: int):
    with open(source, 'rb') as file:
        with open(os.path.join(directory, os.path.basename(source)), 'wb') as filte_write:
            for buffer in iter(partial(file.read, buffer_size), b''):
                filte_write.write(buffer)


ex5('./9.py', './5', 10)
