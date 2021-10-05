start:
             0x0200     CLS
             0x0202     LD I, lbl_0x022a
             0x0204     LD V0, 0x0c
             0x0206     LD V1, 0x08
             0x0208     DRW V0, V1, 0x0f
             0x020a     ADD V0, 0x09
             0x020c     LD I, lbl_0x0239
             0x020e     DRW V0, V1, 0x0f
             0x0210     LD I, lbl_0x0248
             0x0212     ADD V0, 0x08
             0x0214     DRW V0, V1, 0x0f
             0x0216     ADD V0, 0x04
             0x0218     LD I, lbl_0x0257
             0x021a     DRW V0, V1, 0x0f
             0x021c     ADD V0, 0x08
             0x021e     LD I, lbl_0x0266
             0x0220     DRW V0, V1, 0x0f
             0x0222     ADD V0, 0x08
             0x0224     LD I, lbl_0x0275
             0x0226     DRW V0, V1, 0x0f
lbl_0x0228:
             0x0228     JP lbl_0x0228
lbl_0x022a:
             0x022a     DB 0xff    ; 11111111
             0x022b     DB 0x00    ;         
             0x022c     DB 0xff    ; 11111111
             0x022d     DB 0x00    ;         
             0x022e     DB 0x3c    ;   1111  
             0x022f     DB 0x00    ;         
             0x0230     DB 0x3c    ;   1111  
             0x0231     DB 0x00    ;         
             0x0232     DB 0x3c    ;   1111  
             0x0233     DB 0x00    ;         
             0x0234     DB 0x3c    ;   1111  
             0x0235     DB 0x00    ;         
             0x0236     DB 0xff    ; 11111111
             0x0237     DB 0x00    ;         
             0x0238     DB 0xff    ; 11111111
lbl_0x0239:
             0x0239     DB 0xff    ; 11111111
             0x023a     DB 0x00    ;         
             0x023b     DB 0xff    ; 11111111
             0x023c     DB 0x00    ;         
             0x023d     DB 0x38    ;   111   
             0x023e     DB 0x00    ;         
             0x023f     DB 0x3f    ;   111111
             0x0240     DB 0x00    ;         
             0x0241     DB 0x3f    ;   111111
             0x0242     DB 0x00    ;         
             0x0243     DB 0x38    ;   111   
             0x0244     DB 0x00    ;         
             0x0245     DB 0xff    ; 11111111
             0x0246     DB 0x00    ;         
             0x0247     DB 0xff    ; 11111111
lbl_0x0248:
             0x0248     DB 0x80    ; 1       
             0x0249     DB 0x00    ;         
             0x024a     DB 0xe0    ; 111     
             0x024b     DB 0x00    ;         
             0x024c     DB 0xe0    ; 111     
             0x024d     DB 0x00    ;         
             0x024e     DB 0x80    ; 1       
             0x024f     DB 0x00    ;         
             0x0250     DB 0x80    ; 1       
             0x0251     DB 0x00    ;         
             0x0252     DB 0xe0    ; 111     
             0x0253     DB 0x00    ;         
             0x0254     DB 0xe0    ; 111     
             0x0255     DB 0x00    ;         
             0x0256     DB 0x80    ; 1       
lbl_0x0257:
             0x0257     DB 0xf8    ; 11111   
             0x0258     DB 0x00    ;         
             0x0259     DB 0xfc    ; 111111  
             0x025a     DB 0x00    ;         
             0x025b     DB 0x3e    ;   11111 
             0x025c     DB 0x00    ;         
             0x025d     DB 0x3f    ;   111111
             0x025e     DB 0x00    ;         
             0x025f     DB 0x3b    ;   111 11
             0x0260     DB 0x00    ;         
             0x0261     DB 0x39    ;   111  1
             0x0262     DB 0x00    ;         
             0x0263     DB 0xf8    ; 11111   
             0x0264     DB 0x00    ;         
             0x0265     DB 0xf8    ; 11111   
lbl_0x0266:
             0x0266     DB 0x03    ;       11
             0x0267     DB 0x00    ;         
             0x0268     DB 0x07    ;      111
             0x0269     DB 0x00    ;         
             0x026a     DB 0x0f    ;     1111
             0x026b     DB 0x00    ;         
             0x026c     DB 0xbf    ; 1 111111
             0x026d     DB 0x00    ;         
             0x026e     DB 0xfb    ; 11111 11
             0x026f     DB 0x00    ;         
             0x0270     DB 0xf3    ; 1111  11
             0x0271     DB 0x00    ;         
             0x0272     DB 0xe3    ; 111   11
             0x0273     DB 0x00    ;         
             0x0274     DB 0x43    ;  1    11
lbl_0x0275:
             0x0275     DB 0xe0    ; 111     
             0x0276     DB 0x00    ;         
             0x0277     DB 0xe0    ; 111     
             0x0278     DB 0x00    ;         
             0x0279     DB 0x80    ; 1       
             0x027a     DB 0x00    ;         
             0x027b     DB 0x80    ; 1       
             0x027c     DB 0x00    ;         
             0x027d     DB 0x80    ; 1       
             0x027e     DB 0x00    ;         
             0x027f     DB 0x80    ; 1       
             0x0280     DB 0x00    ;         
             0x0281     DB 0xe0    ; 111     
             0x0282     DB 0x00    ;         
             0x0283     DB 0xe0    ; 111     

