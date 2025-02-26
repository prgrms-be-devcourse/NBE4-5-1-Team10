import { components } from "@/lib/backend/generated/schema";
import ClientPage from "./client-page";
import client from "@/lib/backend/client";
import { cookies } from "next/headers";
import { toast } from "sonner";

export type CartItemDto = components["schemas"]["CartItemDto"] & {
  selected: boolean;
};

export async function postOrder(
  address: string,
  postalCode: string,
  items: CartItemDto[]
) {
  const token = (await cookies()).get("accessToken");
  if (!token) {
    return;
  }
  const response = await client.POST("/api/v1/order", {
    headers: {
      Authorization: `Bearer ${token?.value}`,
    },
    body: {
      address,
      postalCode,
      orderItems: items.map((item) => {
        return {
          productId: item.productId,
          price: item.totalPrice,
          quantity: item.quantity,
        };
      }),
    },
    credentials: "include",
  });

  if (response.error) {
    toast.error("로그인 실패", {
      description: "결제 오류입니다.",
    });
    return;
  }
}

export default function Page() {
  return <ClientPage />;
}
