-- begin BOOKSTORE_ONLINE_ORDER
alter table BOOKSTORE_ONLINE_ORDER add constraint FK_BOOKSTORE_ONLINE_ORDER_ON_CUSTOMER foreign key (CUSTOMER_ID) references BOOKSTORE_CUSTOMER(ID)^
create index IDX_BOOKSTORE_ONLINE_ORDER_ON_CUSTOMER on BOOKSTORE_ONLINE_ORDER (CUSTOMER_ID)^
-- end BOOKSTORE_ONLINE_ORDER

-- begin BOOKSTORE_AUTHOR
create unique index IDX_BOOKSTORE_AUTHOR_UK_EMAIL on BOOKSTORE_AUTHOR (EMAIL) where DELETE_TS is null ^
-- end BOOKSTORE_AUTHOR

-- begin BOOKSTORE_CUSTOMER
alter table BOOKSTORE_CUSTOMER add constraint FK_BOOKSTORE_CUSTOMER_ON_USER foreign key (USER_ID) references SEC_USER(ID)^
create index IDX_BOOKSTORE_CUSTOMER_ON_USER on BOOKSTORE_CUSTOMER (USER_ID)^
-- end BOOKSTORE_CUSTOMER

-- begin BOOKSTORE_ORDER_LINE
alter table BOOKSTORE_ORDER_LINE add constraint FK_BOOKSTORE_ORDER_LINE_ON_BOOK foreign key (BOOK_ID) references BOOKSTORE_BOOK(ID)^
alter table BOOKSTORE_ORDER_LINE add constraint FK_BOOKSTORE_ORDER_LINE_ON_ONLINE_ORDER foreign key (ONLINE_ORDER_ID) references BOOKSTORE_ONLINE_ORDER(ID)^
create index IDX_BOOKSTORE_ORDER_LINE_ON_BOOK on BOOKSTORE_ORDER_LINE (BOOK_ID)^
create index IDX_BOOKSTORE_ORDER_LINE_ON_ONLINE_ORDER on BOOKSTORE_ORDER_LINE (ONLINE_ORDER_ID)^
-- end BOOKSTORE_ORDER_LINE

-- begin BOOKSTORE_BOOK_AUTHOR_LINK
alter table BOOKSTORE_BOOK_AUTHOR_LINK add constraint FK_BOOAUT_ON_BOOK foreign key (BOOK_ID) references BOOKSTORE_BOOK(ID)^
alter table BOOKSTORE_BOOK_AUTHOR_LINK add constraint FK_BOOAUT_ON_AUTHOR foreign key (AUTHOR_ID) references BOOKSTORE_AUTHOR(ID)^
-- end BOOKSTORE_BOOK_AUTHOR_LINK
