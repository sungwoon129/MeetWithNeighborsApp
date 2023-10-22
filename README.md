## 내 주변 이웃들과의 소모임 앱 WeYou,I ##

<h3><b>[SWAGGER API 링크](http://20.39.194.127:48080/api-docs/swagger-ui/index.html#/)</b></h3>

### 개요 ###
WeYouI(가제)는 SNS를 통해서 전 세계 사람들과 소통할 수 있지만, 우리 옆집에 사는 이웃과는 소통이 적은 사회에서 이웃들과 조금 더 
친해지길 바라는 목적을 가지고 만든 어플리케이션입니다. 

사람은 관계 속에서 나의 정체성을 확립하고 자존감을 찾습니다. 그래서 다른 사람과의 소통은 '나'에게도 무척이나 중요한 부분입니다.
과거에는 작은 일도 나 혼자 하기에는 버거웠고 다른 사람과 함께 하는 일이 정말 많았습니다. 그 과정에서 타인과 소통을 하고, 관계를 형성해나갔습니다.
그리고 여기서 '타인'은 대부분 나와 사는 장소가 가까운 이웃들이었습니다.
하지만 최근에 코로나라는 전염병이 만연하고, 기술과 과학의 발전으로 이웃들과 직접적으로 소통하지 않고도 대부분의 일을 할 수 있기에 관계를 만들어 나가는 것이 힘들어졌습니다.
그렇다고 무작정 이웃들과 친하게 지내기에는 계기가 없고, 이웃들과 무언가를 함께하는 것이 익숙하지 않고 어색한 사람들도 많습니다.

그래서 WeYou,I는 내 주변의 이웃들과 같이 활동하는 계기를 만들어주고 지속적인 모임이 아닌 1회성 모임을 추구합니다.
만난 이웃이 나와 잘 안맞을 수도 있고, 지속적인 모임을 가진다는 것은 부담으로 다가올 수도 있습니다. 
하지만 길을 지나가다, 그냥 오늘 혼자 밥먹기 싫어서, 오늘 운동하기 귀찮아서 함께 동기부여할 사람이 필요할 수도 있습니다.
그게 가까운 이웃이면 조금 더 마음의 벽이 얇아지고 부담이 덜 할 것입니다. 가까운 이웃과 다양한 활동을 하는 도중, 하고나서 동네의 멋진 가게들을 방문해서
식사도 할 수 있고, 더 다양한 취미도 함께 즐길 수 있습니다. 또한 이웃과 관계를 형성한다는 것은 자존감을 높여주고 삶에 활력을 줍니다.
그렇게되면 최근 사회적으로 대두되는 청년들의 고독사문제나 구직포기청년의 증가등과 같은 사회문제 해결에도 조금이나마 기여할 수 있다고 생각합니다.

조금 더 구체적인 내용은 깃허브에 업로드 해놓은 [위유아이 제안.pptx](https://github.com/sungwoon129/MeetWithNeighborsApp/files/11637425/default.pptx)
파일을 다운받아 확인할 수 있습니다.

### 요구사항 ###

0. 공통
+ 모든 엔티티는 등록시간과 최종수정시간 데이터를 가진다.

1. 회원
+ 회원 등록
+ 로그인
  + 로그인 방식은 소셜 로그인 방식과 회원 로그인 방식이 존재한다.
+ 회원정보 수정
+ 회원 탈퇴
+ 회원 정보
+ 회원은 1개 이상의 모임에 소속될 수 있다.

2. 모임
+ 모임 등록
    + 모임은 카테고리로 구분하며, 카테고리는 '운동','산책','친목','식사','취미'가 있다.
    + 모임은 활동여부에 따라 상태가 존재하며, 상태는 '활동 전','활동 중','활동 종료'가 있다.
    + 모임 활동주소는 변경이 가능하다.
    + 모임 등록시 반드시 모임 시작시간을 지정해야 하며, 시작시간이 되면 모임의 상태는 '활동 중'으로 변경된다.
    + 모임 개설시 모임의 카테고리에 맞는 모임장소를 추천한다.
+ 모임 정보 수정
+ 모임 삭제
+ 모임 정보
+ 모임은 1회성으로만 존재하며, 활동이 종료된 후에는 활동종료 상태가 되며, 활동이력만 남는다.
+ 모임의 활동이 종료되면 모임의 카테고리와 관련이 있는 평점이 높은 가게 중 일부를 추천한다.
+ 모임 목록 조회
    + 조회를 요청하는 회원의 주소를 기준으로 반경 3km 내의 활동주소를 가지는 모임만 불러온다.
+ 모임 탈퇴
+ 모임 참가
    + 모임 참가는 '활동 전', '활동 중'에만 가능하다.
+ 모임 구성원에게 현재 내 위치를 알릴 수 있는 기능이 존재한다. → 현재 내위치를 모임 정보방에 공유한다.
+ 모임 활동이 시작,종료시 모임 구성원들에게 메시지가 전송된다.

3. 가게
+ 가게 등록
+ 가게 정보 수정
+ 가게 삭제
+ 가게 주문내역이 있는 모임은 주문시점으로부터 72시간내에 평점을 매길 수 있다.
+ 가게의 평점은 모임의 구성원 모두가 매길 수 있으며, 구성원이 매긴 평점의 평균을 반영한다. [구현 예정]
+ 가게 목록 조회
    + 가게 목록은 사용자의 현재 위치를 기준으로 한다. 

4. 주문
+ 상품 주문
    + 주문은 모임단위로만 가능하며 모임의 구성원 누구나 모임을 대표해서 주문할 수 있다.
+ 주문 취소
  + 주문취소는 "주문 완료"이외의 상태에서만 가능하다.
  + 주문취소시 주문의 상태가 "결제완료", "
  + 주문취소시 주문의 상태가 "결제 요청"인 경우 환불처리를 한다.
+ 주문 내역 조회
    + 주문 내역 조회시 회원이 속한 모든 모임의 주문내역을 불러오며, 모임별로 주문 필터링이 가능하다.
+ 주문은 "주문","결제요청","결제 완료","주문 확인","주문 완료", "주문 취소"의 상태를 가진다
+ 주문의 상태가 결제 완료, 주문 확인, 주문 완료, 주문 취소시 모임의 구성원들에게 메시지가 전송된다.
+ 주문은 현장결제만 존재하므로 별도의 배송기능은 구현하지 않는다.


5. 상품
+ 상품 등록
  + 상품은 가게에 속한다.
  + 상품등록은 가게만 가능하다
  + 상품은 상품이미지들을 가지고 있다.
+ 상품 정보 조회
  + 상품의 상태는 "판매중","비 판매중" 이 있다.
+ 상품 삭제
+ 상품정보 수정


6. 결제
+ 결제서비스는 외부서비스를 사용한다고 가정하며, 실제로 구현하진 않는다. 서비스 요청까지만 구현한다.

### 기술 스택 ###
+ SpringBoot 3.1
+ Gradle 7.5
+ JPA(Hibernate 6.2.2 Final)
+ MySql
+ Message Queue : RabbitMQ : 주문 알람 처리 및 외부 결제서비스 호출 목적
+ Redis : JWT 토큰정보 관리 목적


### UML ###
![UML](https://github.com/sungwoon129/MeetWithNeighborsApp/assets/43958570/9c5e5fd5-bf60-4fbf-8cf0-77644b20fdf4)



### UI DESIGN ###
전체 스토리보드는 [WeYou,I.pdf](https://github.com/sungwoon129/MeetWithNeighborsApp/files/11898676/WeYou.I.pdf) 에서 확인할 수 있습니다.
아래는 전체 스토리보드 중 일부입니다.
아직 API 구현이 완료되지 않아 화면단은 <b> 미구현 </b> 상태입니다.

![크기변환_홈 _ 로그인 정보가 없는 경우](https://github.com/sungwoon129/MeetWithNeighborsApp/assets/43958570/c81e2c87-7f32-4310-a76f-5ef717e8e31b)
![크기변환_홈(모임) - 메인 - 목록 _ 로그인 정보가 있는 경우](https://github.com/sungwoon129/MeetWithNeighborsApp/assets/43958570/daf4e810-e1a8-430d-8c99-71d9addd5b2e)
![크기변환_홈- 모임활동 상세](https://github.com/sungwoon129/MeetWithNeighborsApp/assets/43958570/c1f0a7e8-7eb7-4f7c-ac5e-df9afaa4adcb)
![크기변환_My - 나의 가게 - 가게관리](https://github.com/sungwoon129/MeetWithNeighborsApp/assets/43958570/b52dea4e-786f-463b-bef9-48d49ce59c03)


![WeYou,I - 전체](https://github.com/sungwoon129/MeetWithNeighborsApp/assets/43958570/7f8bf7d8-7685-4815-b78a-c93ff381a993)


### SWAGGER API ###

<b>[SWAGGER API 링크](http://20.39.194.127:48080/api-docs/swagger-ui/index.html#/)</b>

![image](https://github.com/sungwoon129/MeetWithNeighborsApp/assets/43958570/2184c620-9431-4e1d-a634-0aa87c34b0d8)

![image](https://github.com/sungwoon129/MeetWithNeighborsApp/assets/43958570/af39593a-a375-4242-93d1-4caf788c2ee1)




