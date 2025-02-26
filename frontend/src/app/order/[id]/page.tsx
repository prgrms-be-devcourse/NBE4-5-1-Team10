import { cookies } from "next/headers";
import ClientPage from "./client-page";
import client from "@/lib/backend/client";

export default async function Page({
  params,
}: {
  params: {
    id: number;
  };
}) {
  const { id } = await params;

  const token = (await cookies()).get("accessToken");
  if (!token) {
    return;
  }

  const response = await client.GET("/api/v1/order/{id}", {
    params: {
      path: {
        id: id, // 여기에 실제 orderId 값을 넣어야 함
      },
    },
    headers: {
      Authorization: `Bearer ${token?.value}`,
    },
    credentials: "include",
  });

  if (response.error) {
    return <div>{response["error"]["msg"]}</div>;
  }

  const data = response.data!!;
  const product = data.data!!;

  return <ClientPage order={product} />;
}
