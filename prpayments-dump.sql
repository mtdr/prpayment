PGDMP         4                x            prpayments1    12.4    12.4                0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false                       0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false                       0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false                       1262    16739    prpayments1    DATABASE        CREATE DATABASE prpayments1 WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'Russian_Russia.1251' LC_CTYPE = 'Russian_Russia.1251';
    DROP DATABASE prpayments1;
                postgres    false            Κ            1259    16740    participant    TABLE     ]   CREATE TABLE public.participant (
    id bigint NOT NULL,
    name character varying(255)
);
    DROP TABLE public.participant;
       public         heap    postgres    false            Λ            1259    16745    payment    TABLE       CREATE TABLE public.payment (
    id bigint NOT NULL,
    amount numeric(14,4),
    date timestamp without time zone,
    shardnum integer,
    receiverid bigint NOT NULL,
    senderid bigint NOT NULL,
    CONSTRAINT payment_amount_check CHECK ((amount >= (0)::numeric))
);
    DROP TABLE public.payment;
       public         heap    postgres    false            
          0    16740    participant 
   TABLE DATA           /   COPY public.participant (id, name) FROM stdin;
    public          postgres    false    202   j                 0    16745    payment 
   TABLE DATA           S   COPY public.payment (id, amount, date, shardnum, receiverid, senderid) FROM stdin;
    public          postgres    false    203          
           2606    16744    participant participant_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.participant
    ADD CONSTRAINT participant_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.participant DROP CONSTRAINT participant_pkey;
       public            postgres    false    202            
           2606    16750    payment payment_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.payment
    ADD CONSTRAINT payment_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.payment DROP CONSTRAINT payment_pkey;
       public            postgres    false    203            
           2606    16752 %   participant uc_participantentity_name 
   CONSTRAINT     `   ALTER TABLE ONLY public.participant
    ADD CONSTRAINT uc_participantentity_name UNIQUE (name);
 O   ALTER TABLE ONLY public.participant DROP CONSTRAINT uc_participantentity_name;
       public            postgres    false    202            
           2606    16754 8   payment uc_paymententity_senderid_receiverid_amount_date 
   CONSTRAINT        ALTER TABLE ONLY public.payment
    ADD CONSTRAINT uc_paymententity_senderid_receiverid_amount_date UNIQUE (senderid, receiverid, amount, date);
 b   ALTER TABLE ONLY public.payment DROP CONSTRAINT uc_paymententity_senderid_receiverid_amount_date;
       public            postgres    false    203    203    203    203            
           2606    16755 #   payment fkkfq7qf05cn7cjnh6cmkoc3c16    FK CONSTRAINT        ALTER TABLE ONLY public.payment
    ADD CONSTRAINT fkkfq7qf05cn7cjnh6cmkoc3c16 FOREIGN KEY (receiverid) REFERENCES public.participant(id);
 M   ALTER TABLE ONLY public.payment DROP CONSTRAINT fkkfq7qf05cn7cjnh6cmkoc3c16;
       public          postgres    false    2691    202    203            
           2606    16760 #   payment fkobtfshy8qqs9h6lpnjbfabbii    FK CONSTRAINT        ALTER TABLE ONLY public.payment
    ADD CONSTRAINT fkobtfshy8qqs9h6lpnjbfabbii FOREIGN KEY (senderid) REFERENCES public.participant(id);
 M   ALTER TABLE ONLY public.payment DROP CONSTRAINT fkobtfshy8qqs9h6lpnjbfabbii;
       public          postgres    false    2691    203    202            
      xΡγββ Ε ©            xΡγββ Ε ©     