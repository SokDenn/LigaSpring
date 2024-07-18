insert into app_user (id, name, username, password, active) values
    ('76c9280e-ddb5-417e-aa9e-d2118c0fb0ea', 'Иван', 'ivan', '$2a$08$.IZ.7xywwcbqRxyXxyNotOLiyoEGBr.oLloaLWFHj1uyb8fMjUW8m', true),
    ('b551ef36-eb55-43fd-b889-720dab646276', 'Денис', 'den', '$2a$08$XcYS/Q1NCehF..Yx0sJx1e0/m2PpoGZs63qUWtH7v70Da6vBtQl9S', true),
    ('7c082c41-b2e8-492f-888f-f3d95bd1ccb6', 'Админ', 'admin', '$2a$08$b3h2q1b5GzzDJ/Yv.9rDXe8DmNtVs1xwtYSf2ZPviZMnT4ND5P6gO', true);

insert into project (id, heading, description) values
    ('517dc930-a8f9-4e7c-9777-2c7ed3d0ca7a', 'Это самый первый проект',  'Нужно сделать то то и то, а ещё вон то'),
    ('cca85c1d-e6d1-4e37-ae3a-2adb164735bb', 'Ожидаемый проект', 'Когда то здесь будет его описание'),
    ('88bed8da-03f1-4010-8637-b801c687f3b1',  'Супер большой и новый проект!', 'Описание проекта на выходных');

insert into role (id, name) values
    ('8bbd529d-4641-449c-a0ef-bed24b65aff1', 'ADMIN'),
    ('5d55841f-53be-4c8b-a4ec-07e8d57df049', 'USER'),
    ('2bfac422-3c09-4fd7-951a-41d371c95ac0', 'DIRECTOR');

insert into role_user(role_id, user_id) values
    ('2bfac422-3c09-4fd7-951a-41d371c95ac0', '76c9280e-ddb5-417e-aa9e-d2118c0fb0ea'),
    ('5d55841f-53be-4c8b-a4ec-07e8d57df049', 'b551ef36-eb55-43fd-b889-720dab646276'),
    ('8bbd529d-4641-449c-a0ef-bed24b65aff1', '7c082c41-b2e8-492f-888f-f3d95bd1ccb6');

insert into project_user(project_id, user_id) values
    ('88bed8da-03f1-4010-8637-b801c687f3b1', 'b551ef36-eb55-43fd-b889-720dab646276'),
    ('517dc930-a8f9-4e7c-9777-2c7ed3d0ca7a', '76c9280e-ddb5-417e-aa9e-d2118c0fb0ea'),
    ('cca85c1d-e6d1-4e37-ae3a-2adb164735bb', '76c9280e-ddb5-417e-aa9e-d2118c0fb0ea'),
    ('cca85c1d-e6d1-4e37-ae3a-2adb164735bb', 'b551ef36-eb55-43fd-b889-720dab646276');

insert into task (id, heading, description, date_of_completion, status, project_id, user_id) values
    ('9a0f53b4-f800-4d2e-b7d2-376f56b3ba26', 'Выполнить ДЗ', 'Придумать и написать кучу код', '2024-01-25', 'DONE', '517dc930-a8f9-4e7c-9777-2c7ed3d0ca7a', 'b551ef36-eb55-43fd-b889-720dab646276'),
    ('71894b0a-378a-4981-a920-098921117005', 'Проверить код', 'Посмотреть ДЗ и сказать вау', '2024-02-25', 'IN_WORK', '517dc930-a8f9-4e7c-9777-2c7ed3d0ca7a', '76c9280e-ddb5-417e-aa9e-d2118c0fb0ea'),
    ('22cc7a2c-84c8-4864-b2f2-1293e0249200', 'Оценить код', 'Выставить оценку по ДЗ', '2024-03-25', 'NEW', '517dc930-a8f9-4e7c-9777-2c7ed3d0ca7a', '76c9280e-ddb5-417e-aa9e-d2118c0fb0ea'),
    ('8983fdbe-896a-4268-8a6b-9c3e6798eaec', 'Выполнить большое ДЗ', 'Придумать и написать кучу побольше кода', '2024-04-25', 'IN_WORK', '88bed8da-03f1-4010-8637-b801c687f3b1', 'b551ef36-eb55-43fd-b889-720dab646276'),
    ('504bdc3c-dc1a-4388-9389-58366d7b2e51', 'Скипнуть', 'Не проверять и отправить переделывать', '2024-05-25', 'NEW', '88bed8da-03f1-4010-8637-b801c687f3b1', '76c9280e-ddb5-417e-aa9e-d2118c0fb0ea'),
    ('fc7f12bb-46d0-4a28-a94f-690475b1489c', 'Переделать все', 'Вообще все', '2024-06-25', 'NEW', '88bed8da-03f1-4010-8637-b801c687f3b1', 'b551ef36-eb55-43fd-b889-720dab646276'),
    ('f9201bf4-d9db-4a69-b13e-108731947688', 'Смотреть в потолок', '...', '2024-07-25', 'NEW', '88bed8da-03f1-4010-8637-b801c687f3b1', '7c082c41-b2e8-492f-888f-f3d95bd1ccb6');