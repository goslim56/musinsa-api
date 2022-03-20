[![N|Solid](https://jpassets.jobplanet.co.kr/production/uploads/company/logo/355596/thumb_logo-thumb_3x.png)](https://www.musinsa.com/app/)
- # 김경한 무신사/개발 과제



- ## 개발 환경
  |구분|제품명|Version|
      |------|---|---|
  |개발언어|Java|11|
  |Framework|SpringBoot|2.6|
  |데이터베이스|H2|2.1.210|


- ### 데이터베이스 접속정보 (H2)
- DB 이름: musinsa-test
- url: jdbc:h2:tcp://localhost/~/musinsa-test;
- username: sa
- password:

- ### 테이블 생성 쿼리문
    ```
    create table category (
        id bigint generated by default as identity,
        category_level bigint default 0,
        name varchar(20) not null,
        parent_id bigint,
        primary key (id)
    )
  
  
    ```
- ### 테스트 방법
1. POST /category
    - 카테고리 등록
    - | Parameter|Required|Description|
        |------|---|---|
        | name|true|카테고리 이름|
        | parentId |false|상위 카테고리 번호|
        
2. PUT /category
    - 카테고리 수정
    - | Parameter|Required| Description |
      |------|-------------|---|
      | id|true| 카테고리 번호     |
      | name |false| 카테고리 이름  |
      | parentId |false|상위 카테고리 번호|


3. DELETE /category
    - 카테고리 삭제
   - | Parameter|Required| Description |
           |------|-------------|---|
     | id|true| 카테고리 번호     |


4. GET /categories/{id}
    - 상위 카테고리를 이용해, 카테고리 조회 (하위카테고리 포함)
    - response Sample
        ```
       {
            "categories": [
               {
                  "id": 3,
                  "name": "테스트 카테고리",
                  "categoryLevel": 2
                  "parentId": 2,
                  "child": []
                }
              ]
        }
        ```

5. GET /categories
    - 전체 카테고리 조회
        - response Sample
        ```
       {
            "categories": [
               {
                    "id": 1,
                    "name": "카테고리 테스트",
                    "categoryLevel": 0,
                    "parentId": null
                }
              ]
        }
        ```
     