import { cookies } from "next/headers";
import ClientPage from "./client-page";
import client from "@/lib/backend/client";

export default async function Page() {
  const token = (await cookies()).get("accessToken");
  if (!token) {
    return;
  }

  const response = await client.GET("/api/v1/orders", {
    headers: {
      Authorization: `Bearer ${token?.value}`,
    },
    credentials: "include",
  });

  const data = response.data!!;

  const order = data.data!!;

  return <ClientPage orders={order} />;
}
