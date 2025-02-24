import ClientPage from "./client-page";
import client from "@/lib/backend/client";

export const updateCartAPI = async (productId: number, quantity: number) => {
  const response = await client.PATCH("/api/v1/cart", {
    body: {
      productId,
      quantity,
    },
  });
  if (response.error) return;
};

export const getCartAPI = async () => {
  const response = await client.GET("/api/v1/cart", {
    credentials: "include",
  });

  if (response.error) {
    alert(response["error"]["msg"]);
    return;
  }
  return response.data.data!!;
};
export default async function Page() {
  // const data = getCartAPI();

  const cart = {
    id: 1,
    userId: 2,
    cartItems: [
      {
        id: 3,
        productId: 3,
        productName: "케냐 AA",
        productImageUrl:
          "https://groasting.com/web/product/big/202011/f03b4f32fb0d5714f9ef87cdd8d53b68.jpg",
        quantity: 10,
        price: 17000,
        totalPrice: 170000,
      },
    ],
  };

  return <ClientPage data={cart} />;
}
