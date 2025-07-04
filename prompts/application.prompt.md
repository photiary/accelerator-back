# Junie Guidelines for an Application that Manages Prompts.

# Features

* 시퀀스 다이어그램을 관리하는 어플리케이션 서버.
* 시퀀스 다이어그램을 이용하여 Java 서비스 어플리케이션 코드를 생성할 수 있는 프롬프트 생성.

## Domain

### Folder

* 기능을 관리하기 위한 폴더이다.
* 화면에서 Tree 구조로 표현한다.
* Folder 안에는 Folder 와 Feature를 생성할 수 있다.
* Feature 안에는 생성할 수 없다.

* Domain: Folder
* Entity:
  * id
  * name
  * description
  * hasMany: Folders 
  * hasMany: Features

### Feature

* AI-Agent에 사용할 TemplatePrompt, SequenceDiagram, Query를 조합하여 프롬프트를 생성할 수 있도록 관리한다.

* Domain: Feature
* Entity:
  * id
  * name
  * description
  * TemplatePrompt
  * SequenceDiagram
  * Query

* API:
  * responseFeature:
    * Feature 도메인 
    * TemplatePrompt, SequenceDiagram, Query 도메인의 ID와 Content를 응답한다.
  * createFeature:
    * TemplatePrompt는 선택한 ID를 전달 받는다. 
    * SequenceDiagram, TemplatePrompt, Query도 같이 생성하기위해 각 도메인의 ID는 전달 받지 않는다.
  * updateFeature:
    * TemplatePrompt: 수정한 ID를 전달 받는다.
    * SequenceDiagram, Query 도메인을 같이 수정한다. 수정 대상 도메인의 ID와 Content를 전달 받는다.
  

### Sequence Diagram

* 화면에서 작성한 Mermaid 코드를 관리
* Mermaid is a JavaScript-based diagramming and charting tool that renders Markdown-inspired text definitions to create and modify diagrams dynamically.
* sequenceDiagramContent 데이터 샘플
```
sequenceDiagram
    Alice->>John: Hello John, how are you?
    John-->>Alice: Great!
    Alice-)John: See you later!
```

* Domain: SequenceDiagram
* Entity
  * id
  * name
  * sequenceDiagramContent

### Prompt

* Domain: TemplatePrompt
* Entity
  * id
  * name
  * promptContent

### Query

* SQL Query를 관리

* Domain: SqlQuery
* Entity
  * id
  * name
  * queryContent