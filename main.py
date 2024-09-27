import os
import requests
import tiktoken

# GPT-3.5-turbo API 설정
API_URL = "https://api.openai.com/v1/chat/completions"
API_KEY = "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"
HEADERS = {"Content-Type": "application/json", "Authorization": f"Bearer {API_KEY}"}

GPT_MODEL = "gpt-4"
TOKEN_LIMIT_RATIO = 0.8
TOKEN_LIMIT = 8192
TOKEN_THRESHOLD = int(TOKEN_LIMIT * TOKEN_LIMIT_RATIO)

tokenizer = tiktoken.get_encoding("p50k_base")


def count_tokens(text):
    try:
        return len(tokenizer.encode(text))
    except Exception as e:
        print(f"에러: {e}")
        return 0


def split_into_parts(content):
    lines = content.split("\n")
    parts = []
    part = ""
    for line in lines:
        if count_tokens(part + line) > TOKEN_THRESHOLD:
            parts.append(part)
            part = line + "\n"
        else:
            part += line + "\n"
    parts.append(part)
    return parts


def analyze_code(requirement, code):
    payload = {
        "model": GPT_MODEL,
        "temperature": 0.7,
        "messages": [{"role": "user", "content": f"{requirement}: \n```\n{code}\n```"}],
    }

    response = requests.post(API_URL, headers=HEADERS, json=payload)

    if response.status_code != 200:
        print(f"API 요청 실패: HTTP 상태 코드 {response.status_code}")
        print(f"응답 내용: {response.text}")
        return None

    try:
        response_data = response.json()
    except ValueError:
        print("응답이 JSON 형식이 아닙니다.")
        return None

    return response_data["choices"][0]["message"]["content"].strip()


current_folder = os.path.dirname(os.path.abspath(__file__))


def write_markdown_report(file_path, file_relative_path, parts_analysis, mode="a"):
    with open(file_path, mode, encoding="utf-8") as md_file:
        for part_number, analysis in parts_analysis.items():
            if len(parts_analysis) == 1:
                md_file.write(f"### {file_relative_path}\n")
            else:
                md_file.write(f"### {file_relative_path} - part {part_number}\n")
            md_file.write(f"\n{analysis}\n\n")


def process_large_files(directory, file_ext, requirement):
    report_file_path = os.path.join(current_folder, "report.md")
    if os.path.exists(report_file_path):
        os.remove(report_file_path)

    for root, dirs, files in os.walk(directory):
        for file_name in files:
            if not file_name.endswith(file_ext):
                continue

            print("* " + file_name)

            file_path = os.path.join(root, file_name)
            file_relative_path = os.path.relpath(file_path, start=directory)

            with open(
                file_path, "r", encoding="utf-8", errors="ignore"
            ) as file_content:
                content = file_content.read()

            parts = split_into_parts(content)
            parts_analysis = {}
            for i, part in enumerate(parts, start=1):
                analysis = analyze_code(requirement, part)
                if analysis:
                    parts_analysis[str(i)] = analysis

            write_markdown_report(report_file_path, file_relative_path, parts_analysis)


folder_path = "/Users/user/Desktop/CODE/Project"
file_ext = ".java"
requirement = "전체 파일을 스캔하고 만들어야 될 부분을 알려줘"
# requirement = "코드를 리팩토링 대상을 찾아줘."
process_large_files(folder_path, file_ext, requirement)
