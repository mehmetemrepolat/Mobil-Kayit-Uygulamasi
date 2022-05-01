
![önizleme](https://user-images.githubusercontent.com/97759584/166165253-ce03d8df-caaa-4cd0-8ef9-7128d481799f.png)

![önizleme2](https://user-images.githubusercontent.com/97759584/166165260-19ba5636-a131-4007-8ed7-afd23ee6ce9e.png)


# Mobile_Registration_Form
Mobile Registration Form Application that receives login information from the user, 
signs and verifies the accuracy of the information in the mobile signature section of the application and saves the information to the MSSQL database.
     

# Mobile_Registration_Form
Kullanıcıdan giriş bilgilerini alan Mobil Kayıt Formu Uygulaması,
uygulamanın mobil imza bölümünden bilgilerin doğruluğunu imzalayıp doğrulayabilir ve daha sonrasında bilgileri MSSQL veri tabanına kaydedebilirsiniz.



# Örnek veritabanı tablosu için aşağıdaki komutu çalıştırabilirsiniz
# You can run the code for the sample database table

USE [Deneme]
GO

/****** Object:  Table [dbo].[Ziyaretci_Kayit_Tablosu]    Script Date: 3.02.2022 00:06:15 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Ziyaretci_Kayit_Tablosu](
	[Ziyaretci_Ad] [varchar](50) NOT NULL,
	[Ziyaretci_SoyAd] [varchar](50) NOT NULL,
	[Ziyaretci_TC_Kimlik] [varchar](11) NOT NULL,
	[Ziyaretci_Telefon_Numarasi] [varchar](11) NOT NULL,
	[Ziyaretci_Mail_Adresi] [varchar](100) NOT NULL,
	[Ziyaretci_Sebep] [varchar](150) NOT NULL,
	[Ziyaretci_Imza] [text] NOT NULL,
 CONSTRAINT [PK_Ziyaretci_Kayit_Tablosu] PRIMARY KEY CLUSTERED 
(
	[Ziyaretci_TC_Kimlik] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO


