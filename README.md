# Community
개인프로젝트 - 커뮤니티 사이트 기능 구현

API 테스트 페이지 - 서버 실행 후 [swagger페이지](http://localhost:8080/swagger-ui/index.html#)

## 기술 스택
<img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat-square&logo=Spring%20Boot&logoColor=black"/><img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">


## **프로젝트 주제**

- 커뮤니티 프로젝트

## **프로젝트 구조**

![image](https://user-images.githubusercontent.com/79897135/215305624-72b42d78-0551-49c7-843c-e9e08853072b.png)

## ERD

![image](https://user-images.githubusercontent.com/79897135/215305618-62647ed1-e612-4628-90f0-c2321e22be68.png)

해당 내용대로 진행, 추가로 필요한 데이터가 있을시에 추가

+)회원 테이블에 닉네임이 필요하다고 생각

## 사용 기술 스택

- SpringBoot
- Java
- MySQL
- JPA

## 프로젝트 기능

- 팀이 선택한 주제별 구현해야 하는 기능에 대해 상세 요구사항을 작성해주세요.

<aside>
💡 주제별 구현 기능

- **주제 3. 커뮤니티 과제**
    - [ ]  게시글 관리 기능(게시글 작성/게시글 목록 조회/작성된 게시글 편집/ 게시글 삭제)
        
        → 일반 회원은 로그인시 자신의 게시글만 편집 및 삭제 가능
        
    - [ ]  게시판 글 이력조회
        
        →작성자 닉네임을 토대로 게시판 글의 이력을 조회하는 기능
        
    - [ ]  로그인 / 로그아웃에 따른 편집 허가 기능 구현
        
        → 일반 회원은 로그인시 자신의 게시글만 편집 및 삭제 가능
        
        → 관리자는 모든 게시글 삭제 가능
        
    - [ ]  게시글 작성
        
        → 회원가입이 승인된 모든 회원에 대해 게시글 작성 가능
        
        → 게시글에 이미지 업로드 가능
        
        → 댓글 작성 가능
        
    - [ ]  게시글 목록 조회, 검색 기능
        
        → 관리자 및 일반회원 모두 게시글의 제목, 내용, 작성자 닉네임에 대해서 게시글을 조회, 검색 하는 기능
        
    - [ ]  게시글 편집 및 삭제
        
        → 일반 회원은 로그인시 자신의 게시글만 편집 및 삭제 가능
        
        → 관리자는 전체 게시글에 대한 편집 및 삭제 가능
        
    
</aside>

**회원가입과 로그인**

- 회원가입
    - 회원가입시 이메일, 이름, 전화번호, 비밀번호 정보가 필요하다
    
    (관리자일 경우 db에서 직접 처리하여 관리자 처리를 수행한다) 
    
    - 회원가입시 이미 회원가입된 이메일로 회원가입을 시도하면 에러를 발생한다
    - 이메일로 회원 인증이 되지 않으면 회원가입 처리를 하지 않는다
    
- 로그인
    - 로그인시 회원가입한적 없는 이메일을 이용하여 로그인을 시도하면 에러가 발생한다
    - 로그인시 비밀번호가 일치하지 않는다면 에러가 발생한다.
    
- 비밀번호 변경
    - 비밀번호를 찾는것은 암호가 해시코드로 암호화 되기 때문에 알기 힘들다. 비밀번호를 찾는것 대신 관리자가 임의로 변경 후 사용자가 초기화 할 수 있도록 한다.

**게시판 글 작성**

- 로그인한 유저는 게시판 글을 작성할 수 있다
    - 로그인한 유저에게만 게시글 작성 페이지가 보이도록 한다
    - 관리자 역시 게시글 작성 페이지가 보이고 작성 또한 가능하다
    - 게시글 작성은 이미지 역시 첨부 가능하다 (네이버 스마트에디터 활용)

**게시판 글 목록 조회**

- 로그인한 유저는 게시판 글을 기간별로 조회할 수 있다

**게시판 글 상세보기**

- 게시판의 글은 로그인 한 유저만 볼 수 있다
    - 유저가 로그인하지 않았다면 에러를 발생한다
- 게시판 글 상세보기에서는 제목, 작성일, 수정일, 작성자, 본문의 내용이 보인다.

**게시판 글 수정**

- 유저는 자신이 쓴 게시글만 수정할 수 있다

**게시판 글 삭제**

- 유저는 자신이 쓴 게시글만 삭제할 수 있다
- 관리자는 모든 유저의 게시글을 삭제할 수 있다


## 기능 정의

### 사용자(user-rest-controller)

* 회원가입

사용자 개인정보 입력
![image](https://user-images.githubusercontent.com/79897135/214002840-e10197a8-148c-4ecb-b664-6bd56b88884c.png)

인증 메일 전송
![image](https://user-images.githubusercontent.com/79897135/214002918-593e3dbe-f474-483f-8d3f-457fd7ef06d1.png)

메일을 통한 인증이 이루어 져야만 로그인 가능



* 로그인

로그인 시도
![image](https://user-images.githubusercontent.com/79897135/214005322-1e5b21c4-9295-41de-8ec8-38b0981dfd69.png)


메일 인증을 하지 않아 로그인에 실패한 경우입니다.
![image](https://user-images.githubusercontent.com/79897135/214005399-10002959-08af-4b00-bd98-9a8683c9e5e5.png)


메일로 인증 과정을 실행합니다.
![image](https://user-images.githubusercontent.com/79897135/214002918-593e3dbe-f474-483f-8d3f-457fd7ef06d1.png)
![image](https://user-images.githubusercontent.com/79897135/214005421-34c76c22-7fcf-4a1e-9360-1e0102348843.png)


메일 인증 후 로그인하여 성공한 경우 입니다.
![image](https://user-images.githubusercontent.com/79897135/214005568-9497e129-aa23-487e-983a-182f76b3afd3.png)


* 아이디 찾기

찾고자 하는 사용자의 이름과 연락처를 입력해 아이디를 조회합니다.
![image](https://user-images.githubusercontent.com/79897135/214005674-e87e2c7f-9867-4484-a1bc-7a339e0375ec.png)


* 비밀번호 수정

유저 정보를 입력합니다.id값은 유저 고유의 회원번호 값입니다.
![image](https://user-images.githubusercontent.com/79897135/214005777-b91da5ba-b916-4778-b1aa-df8b9ddd7309.png)


실행 결과
![image](https://user-images.githubusercontent.com/79897135/214006052-95a85ee0-23ac-4ee0-9dac-4fb968d6b8b9.png)

* 회원탈퇴

회원 고유번호를 통해 탈퇴 가능

탈퇴 후 해당 유저와 연관된 모든 entity는 제거 됩니다.
![image](https://user-images.githubusercontent.com/79897135/214047343-626cb071-7271-4a83-b7a4-0ff9e290bc79.png)


### 관리자 (admin-rest-controller)


* 회원조회
 
관리자 번호와 조회하고자 하는 회원번호를 입력해 유저 정보 조회가 가능합니다. 
![image](https://user-images.githubusercontent.com/79897135/214047522-746b4530-0a83-490e-a12b-b8c9002a3d27.png)
 


실행 결과 
![image](https://user-images.githubusercontent.com/79897135/214047538-9d839a22-bb58-457c-885b-452f60ccdd10.png)




### 게시글 (board-rest-controller)

* 게시글 작성

사용자 정보와 제목, 내용, 파일(업로드 할 파일이 있는 경우)을 전송해 게시글 엔티티에 저장합니다.

__(Postman으로 테스트시 파일이 정상적으로 업로드 되지만, swagger 페이지에선 파일이 업로드가 안됩니다.)__
![image](https://user-images.githubusercontent.com/79897135/214048633-1527e0b0-1348-45de-8956-546431381385.png)

![image](https://user-images.githubusercontent.com/79897135/214048856-d8c77035-9f8c-46ab-b945-0862980d6adc.png)



파일이 저장된 모습
![image](https://user-images.githubusercontent.com/79897135/214048868-8db97241-c644-4670-b98a-4e62ea9e416e.png)


* 게시글 조회

게시글을 조회하여 해당 게시글의 정보를 볼 수 있습니다.

![image](https://user-images.githubusercontent.com/79897135/214048946-bca828aa-54cf-41f0-b2cb-e101503a2dfa.png)

![image](https://user-images.githubusercontent.com/79897135/214048960-d483798b-3d55-420c-9d88-4e4c88d2f29c.png)




* 게시글 한번에 여러 페이지 조회

게시글을 한번에 여러 페이지 단위로 조회 할 수 있습니다.

![image](https://user-images.githubusercontent.com/79897135/214049103-45277b36-05d9-4a92-b60b-eadb065b6dde.png)
![image](https://user-images.githubusercontent.com/79897135/214049116-d05c82d1-1937-4462-8abb-737b6ccca650.png)




* 특정 유저가 작성한 글 조회

![image](https://user-images.githubusercontent.com/79897135/214049228-ccd7211b-bcd1-41b2-aa06-628a0cdee66d.png)

![image](https://user-images.githubusercontent.com/79897135/214049247-44d9920b-6b54-4657-a22e-962a3587d1d7.png)




* 게시글 수정

게시글 작성자 정보와 수정을 시도하는 작성자 정보가 일치해야만 수정이 가능합니다.

파일 업로드 역시 수정 가능합니다.

![image](https://user-images.githubusercontent.com/79897135/214049290-4bde2e4e-bc78-458f-bde5-2d45f49454f0.png)
![image](https://user-images.githubusercontent.com/79897135/214049305-03d1326f-ee51-49f2-bd98-bc73fee5045f.png)

수정된 모습
![image](https://user-images.githubusercontent.com/79897135/214049318-0e9d7a08-372a-40de-a293-b1743f868788.png)





* 댓글 작성


![image](https://user-images.githubusercontent.com/79897135/214049767-2315415f-1561-42e1-baa9-af0c2d1ba970.png)
![image](https://user-images.githubusercontent.com/79897135/214049789-72cb6fc6-3b98-4b7d-9ec8-88e1616b7ddd.png)

댓글이 저장된 모습
![image](https://user-images.githubusercontent.com/79897135/214049802-0de1815e-41ac-4a32-86a9-eaa71fa541ab.png)



* 댓글 조회

![image](https://user-images.githubusercontent.com/79897135/214050398-a308f70d-2bac-4e59-8690-cc8ed5690abd.png)
![image](https://user-images.githubusercontent.com/79897135/214050409-8f7eede6-f97a-47ff-9933-ba4fde60d6ab.png)




* 댓글 수정


게시판 수정 역시 작성자와 수정을 시도하는 유저의 아이디가 일치해야만 수정 가능합니다.
![image](https://user-images.githubusercontent.com/79897135/214050455-c2b84795-be8e-4289-87f8-42b0dbe7c182.png)

수정된 모습
![image](https://user-images.githubusercontent.com/79897135/214050485-8e43a27f-acdb-44de-ab60-5978e4ce10cb.png)






* 좋아요 / 싫어요 등록

좋아요 등록
![image](https://user-images.githubusercontent.com/79897135/214050843-6683ce11-8017-4822-9a45-82e52c0dd5f8.png)


등록 결과
![image](https://user-images.githubusercontent.com/79897135/214050855-2d533b19-ce64-4f07-a714-ee344bcf7914.png)


싫어요 등록
![image](https://user-images.githubusercontent.com/79897135/214050951-d5600bcb-1632-44df-a805-76fb748e1665.png)

등록 결과
![image](https://user-images.githubusercontent.com/79897135/214050982-050e806b-e376-481a-ab22-2a03775528b7.png)


해당 게시글 조회시, 좋아요 / 싫어요 등록한 유저 확인 가능
![image](https://user-images.githubusercontent.com/79897135/214051029-0726b4fa-96ae-4d92-bbdc-3c343edc3e57.png)



* 게시글 삭제


![image](https://user-images.githubusercontent.com/79897135/214051186-d52880ce-7637-46b3-97bd-8167b217988f.png)


삭제 결과 
삭제시 연관된 엔티티 모두 삭제
![image](https://user-images.githubusercontent.com/79897135/214051214-5908669b-6894-49ca-9aad-16e9c56fd5d0.png)







로그파일
![image](https://user-images.githubusercontent.com/79897135/214051334-73d4ec92-322f-4317-8470-4553be4b5dfa.png)










