INSERT INTO user(login, password, create_date) VALUES ('email@emuso.com', 'senhaemuso', '2021-02-26');
INSERT INTO user(login, password, create_date) VALUES ('email@secundario.com', 'senhasecundaria', '2021-02-26');
INSERT INTO category(name) VALUES ('categoria-padrao');
INSERT INTO product(`name`, `value`, `amount`, `description`, `category_id`, `owner_id`, `create_date`)
    VALUES ('produto-padrao', 10.00, 1, 'descricao-padrao', 1, 1, '2021-02-26');