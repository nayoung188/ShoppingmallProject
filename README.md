# ShoppingmallProject
통합 쇼핑몰 솔루션 프로젝트

## 📖목차 
- 프로젝트 개요
- 개발환경
- 구현기능 / 상세설명

### 프로젝트 개요
다양한 오픈마켓 API를 사용하여 쇼핑몰을 통합해 손쉬운 쇼핑몰 활용이 가능하다.

### 개발환경
|    Java    |   Spring   | Spring Boot | JavaScript |   Jquery   |   MySQL   |     JPA     |  Spring Security   |  Thymeleaf   |   HTML5   |     CSS    |
| :--------: | :--------: | :--------:  | :--------: | :--------: | :------:  |   :-----:   |  :-------------:   |  :-------:   |   :----:  |    :----:  | 
|<img src="https://img.shields.io/badge/JAVA-007396?style=for-the-badge&logo=java&logoColor=white">| <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white"> |<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=yellow">| <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black"> |<img src="https://img.shields.io/badge/jquery-0769AD?style=for-the-badge&logo=jquery&logoColor=white">| <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">  |

### 구현기능 / 상세설명

#### 메인화면

![메인1-min](https://github.com/nayoung188/ShoppingmallProject/assets/109578804/6f8affac-4fba-43fd-a728-1ea255d07727)

* 메인이미지
  * Slick의 slide 기능을 사용하여 디자인에 다양화를 주었다.

![메인3](https://github.com/nayoung188/ShoppingmallProject/assets/109578804/cd69f945-bdee-4ce4-9850-0311ad90038a)
* 상담문의
  * 모달창을 통해 상담 관련 내용을 작성할 수 있다.

![메인5](https://github.com/nayoung188/ShoppingmallProject/assets/109578804/4ec9b2e6-a6a5-48f2-b29d-7a6c4fc3574a)
* 로그인
  * 로그인 모달창이 나타나면 필요 정보를 입력할 수 있다. Spring Security를 사용한 로그인 처리를 통해 인가된 회원은 해당 회원의 권한에 따라 다른 button이 출력된다. (일반회원 : 대시보드이동 / 관리자 : 관리자페이지이동)

