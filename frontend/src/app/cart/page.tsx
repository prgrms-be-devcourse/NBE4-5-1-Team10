import ClientPage from "./client-page";
import client from "@/lib/backend/client";

export const updateCartAPI = async (
  accessToken: string,
  productId: number,
  quantity: number
) => {
  const response = await client.PATCH("/api/v1/cart", {
    headers: {
      Authorization: "Bearer " + accessToken,
    },
    body: {
      productId,
      quantity,
    },
    credentials: "include",
  });
  if (response.error) return;
};

export const getCartAPI = async (accessToken: string) => {
  const response = await client.GET("/api/v1/cart", {
    headers: {
      Authorization: "Bearer " + accessToken,
    },
    credentials: "include",
  });

  if (response.error) {
    console.log(response["error"]["msg"]);
    return;
  }
  return response.data.data!!;
};
export default async function Page() {
  return <ClientPage />;
}
